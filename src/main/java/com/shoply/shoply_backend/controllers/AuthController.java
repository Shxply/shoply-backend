package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.dto.AuthRequest;
import com.shoply.shoply_backend.services.AuthServices.ShoplyAuthService;
import com.shoply.shoply_backend.services.AuthServices.GoogleAuthService;
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
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        shoplyAuthService.registerUser(request.name, request.email, request.password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        String token = shoplyAuthService.authenticateUser(request.email, request.password);
        return ResponseEntity.ok(token);
    }

}

