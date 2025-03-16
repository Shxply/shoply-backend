package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.services.AuthServices.ShoplyAuthService;
import com.shoply.shoply_backend.services.AuthServices.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/google")
    public ResponseEntity<String> googleAuth(@RequestParam String googleToken) {
        String token = googleAuthService.authenticateGoogleUser(googleToken);
        return ResponseEntity.ok(token);
    }

    // add other OAuth features here
}



