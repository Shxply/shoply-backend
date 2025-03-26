package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.services.AuthServices.ShoplyAuthService;
import com.shoply.shoply_backend.services.AuthServices.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ShoplyAuthService shoplyAuthService;
    private final GoogleAuthService googleAuthService;

    public AuthController(ShoplyAuthService shoplyAuthService, GoogleAuthService googleAuthService) {
        this.shoplyAuthService = shoplyAuthService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        shoplyAuthService.registerUser(name, email, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String token = shoplyAuthService.authenticateUser(email, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/auth/google/login")
    public ResponseEntity<Void> googleLogin(@RequestParam String redirectUri) {
        String oauthUrl = googleAuthService.buildGoogleOAuthUrl(redirectUri);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(oauthUrl))
                .build();
    }


    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String redirectUrl = googleAuthService.handleGoogleCallback(code);
        response.sendRedirect(redirectUrl);
    }
}

