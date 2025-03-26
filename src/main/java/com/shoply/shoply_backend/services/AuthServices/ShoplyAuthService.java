package com.shoply.shoply_backend.services.AuthServices;

import com.shoply.shoply_backend.models.User;
import com.shoply.shoply_backend.repositories.UserRepository;
import com.shoply.shoply_backend.utilities.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.NoSuchElementException;

@Service
public class ShoplyAuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public ShoplyAuthService(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("USER");

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = User.UserFactory.createLocalUser(name, email, hashedPassword, defaultRoles);
        newUser.setProfilePicture("profile-picture/default-profile-picture.png");
        userRepository.save(newUser);

        return jwtUtil.generateToken(newUser.getUserId(), newUser.getRoles());
    }

    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getUserId(), user.getRoles());
    }
}




