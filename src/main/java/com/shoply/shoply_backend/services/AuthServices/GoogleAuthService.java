package com.shoply.shoply_backend.services.AuthServices;

import com.google.api.client.googleapis.auth.oauth2.*;
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

    private final String clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID");
    private final String clientSecret = System.getenv("GOOGLE_OAUTH_CLIENT_SECRET");
    private final String redirectUri = System.getenv("GOOGLE_OAUTH_REDIRECT_URI");

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    public GoogleAuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String buildGoogleOAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=openid profile email"
                + "&prompt=select_account"
                + "&access_type=offline";
    }

    public String handleGoogleCallback(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    HTTP_TRANSPORT,
                    JSON_FACTORY,
                    "https://oauth2.googleapis.com/token",
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            ).execute();

            String idTokenString = tokenResponse.getIdToken();
            return "shoply://auth?token=" + authenticateGoogleUser(idTokenString);

        } catch (IOException e) {
            return "shoply://auth?error=auth_failed";
        }
    }

    public String authenticateGoogleUser(String googleToken) {
        GoogleIdToken.Payload payload = validateGoogleToken(googleToken);
        if (payload == null) {
            throw new SecurityException("Invalid Google token");
        }

        String googleUserId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String profilePicture = (String) payload.get("picture");

        User user = userRepository.findByOauthId(googleUserId).orElseGet(() -> {
            User newUser = User.UserFactory.createOAuthUser("GOOGLE", googleUserId, name, email, profilePicture, Set.of("USER"));
            return userRepository.save(newUser);
        });

        return jwtUtil.generateToken(user.getUserId(), user.getRoles());
    }

    private GoogleIdToken.Payload validateGoogleToken(String googleToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            return idToken != null ? idToken.getPayload() : null;
        } catch (GeneralSecurityException | IOException e) {
            throw new SecurityException("Google token validation failed", e);
        }
    }
}
