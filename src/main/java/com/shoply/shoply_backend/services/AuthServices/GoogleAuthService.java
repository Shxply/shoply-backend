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
import org.tinylog.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
public class GoogleAuthService {

    private final String clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID");
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public GoogleAuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateMobileGoogleUser(String idTokenString) {
        Logger.info("Authenticating mobile Google user");

        GoogleIdToken.Payload payload = validateGoogleToken(idTokenString);
        if (payload == null) {
            Logger.warn("Invalid Google token received");
            throw new SecurityException("Invalid Google token");
        }

        String googleUserId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String profilePicture = (String) payload.get("picture");

        Logger.info("Google user: {} ({})", name, email);

        User user = userRepository.findByOauthId(googleUserId).orElseGet(() -> {
            Logger.info("Creating new user from Google OAuth");
            User newUser = User.UserFactory.createOAuthUser("GOOGLE", googleUserId, name, email, profilePicture, Set.of("USER"));
            return userRepository.save(newUser);
        });

        return jwtUtil.generateToken(user.getUserId(), user.getRoles());
    }

    private GoogleIdToken.Payload validateGoogleToken(String idTokenString) {
        Logger.debug("Validating Google ID token...");
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            boolean valid = idToken != null;
            Logger.debug("Google token validation result: {}", valid);
            return valid ? idToken.getPayload() : null;
        } catch (GeneralSecurityException | IOException e) {
            Logger.error(e, "Token validation failed");
            throw new SecurityException("Google token validation failed", e);
        }
    }
}


