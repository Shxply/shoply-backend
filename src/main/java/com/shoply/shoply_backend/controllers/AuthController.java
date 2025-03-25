package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.services.AuthServices.ShoplyAuthService;
import com.shoply.shoply_backend.services.AuthServices.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/google/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleAuthService.buildGoogleOAuthUrl());
    }

    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam(required = false) String code, HttpServletResponse response) throws IOException {
        String redirectUrl = googleAuthService.handleGoogleCallback(code);
        response.sendRedirect(redirectUrl);
    }
}

