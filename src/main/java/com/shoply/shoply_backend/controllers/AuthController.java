package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.services.AuthServices.ShoplyAuthService;
import com.shoply.shoply_backend.services.AuthServices.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

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

    @PostMapping("/google/mobile")
    public ResponseEntity<?> loginViaMobile(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        if (idToken == null) {
            return ResponseEntity.badRequest().body("Missing idToken");
        }
        String jwt = googleAuthService.authenticateMobileGoogleUser(idToken);
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

}

