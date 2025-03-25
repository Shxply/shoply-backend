package com.shoply.shoply_backend.services.AuthServices;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.shoply.shoply_backend.models.User;
import com.shoply.shoply_backend.repositories.UserRepository;
import com.shoply.shoply_backend.utilities.JwtUtil;
import org.tinylog.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

@Service
public class GoogleAuthService {

    private final String clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID");
    private final String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
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
        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=openid profile email"
                + "&prompt=select_account"
                + "&access_type=offline";

        Logger.info("Generated Google OAuth URL: {}", url);
        return url;
    }

    public String handleGoogleCallback(String code) {
        Logger.info("Handling Google callback with code: {}", code);
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
            Logger.info("Received ID token from Google");

            String jwt = authenticateGoogleUser(idTokenString);
            Logger.info("Generated internal JWT: {}", jwt);

            return "shoply://auth?token=" + jwt;

        } catch (IOException e) {
            Logger.error(e, "Failed to handle Google callback");
            return "shoply://auth?error=auth_failed";
        }
    }

    public String authenticateGoogleUser(String googleToken) {
        Logger.debug("Authenticating Google user with token");
        GoogleIdToken.Payload payload = validateGoogleToken(googleToken);
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

    private GoogleIdToken.Payload validateGoogleToken(String googleToken) {
        Logger.debug("Validating Google ID token...");
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            boolean valid = idToken != null;
            Logger.debug("Google token validation result: {}", valid);
            return valid ? idToken.getPayload() : null;
        } catch (GeneralSecurityException | IOException e) {
            Logger.error(e, "Token validation failed");
            throw new SecurityException("Google token validation failed", e);
        }
    }
}

