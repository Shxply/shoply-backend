package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.annotations.ExpectedResult;
import com.shoply.shoply_backend.annotations.IntegrationTest;
import com.shoply.shoply_backend.annotations.MockDependency;
import com.shoply.shoply_backend.annotations.TestableService;
import com.shoply.shoply_backend.models.User;
import com.shoply.shoply_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@TestableService
@Service
public class UserService {

    @MockDependency
    private UserRepository userRepository;

    public UserService() {
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "[\"user1@example.com\"]", expectedJson = "[{\"userId\":\"1\",\"email\":\"user1@example.com\",\"name\":\"John Doe\"}]")
    @ExpectedResult(inputJson = "[\"nonexistent@example.com\"]", expectedJson = "[]")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "{\"userId\":\"1\",\"email\":\"user1@example.com\",\"name\":\"John Doe\"}")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__NULL__")
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"user1@example.com\"", expectedJson = "{\"userId\":\"1\",\"email\":\"user1@example.com\",\"name\":\"John Doe\"}")
    @ExpectedResult(inputJson = "\"nonexistent@example.com\"", expectedJson = "__NULL__")
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "{\"email\":\"user1@example.com\",\"name\":\"John Doe\"}", expectedJson = "{\"userId\":\"1\",\"email\":\"user1@example.com\",\"name\":\"John Doe\"}")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "__VOID__")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__VOID__")
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}



