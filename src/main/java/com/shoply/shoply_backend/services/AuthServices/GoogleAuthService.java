package com.shoply.shoply_backend.services.AuthServices;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.shoply.shoply_backend.models.User;
import com.shoply.shoply_backend.repositories.UserRepository;
import com.shoply.shoply_backend.utilities.JwtUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Service
public class GoogleAuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String GOOGLE_CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");

    // ✅ Use GsonFactory.getDefaultInstance() (Latest Supported)
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public GoogleAuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateGoogleUser(String googleToken) {
        GoogleIdToken.Payload payload = validateGoogleToken(googleToken);
        if (payload == null) {
            throw new RuntimeException("Invalid Google token");
        }

        String googleUserId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String profilePicture = (String) payload.get("picture");

        User user = userRepository.findByOauthId(googleUserId).orElse(null);

        if (user == null) {
            user = User.UserFactory.createOAuthUser("GOOGLE", googleUserId, name, email, profilePicture, Set.of("USER"));
            userRepository.save(user);
        }

        return jwtUtil.generateToken(user.getUserId(), user.getRoles());
    }

    private GoogleIdToken.Payload validateGoogleToken(String googleToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JSON_FACTORY
        ).setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)).build();

        try {
            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Google token validation failed");
        }

        return null;
    }
}


