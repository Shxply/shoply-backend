package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Globally synchronized time instance
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String oauthId; // Generic OAuth ID (Google, Facebook, etc.)
    private String oauthProvider; // e.g., "GOOGLE", "FACEBOOK", "GITHUB", "APPLE" (Null for local users)
    private String name;
    private String email;
    private String passwordHash; // Null for OAuth users
    private String profilePicture;
    private Set<String> roles; // e.g., ["USER"], ["ADMIN"]
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
    }

    public User(String userId, String oauthId, String oauthProvider, String name, String email, String passwordHash, String profilePicture, Set<String> roles, Instant createdAt, Instant updatedAt) {
        this.userId = userId;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePicture = profilePicture;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUserId(), user.getUserId()) &&
                Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{")
                .append("userId='").append(userId).append('\'')
                .append(", oauthId='").append(oauthId).append('\'')
                .append(", oauthProvider='").append(oauthProvider).append('\'')
                .append(", name='").append(name).append('\'')
                .append(", email='").append(email).append('\'')
                .append(", passwordHash='").append(passwordHash).append('\'')
                .append(", profilePicture='").append(profilePicture).append('\'')
                .append(", roles=").append(roles)
                .append(", createdAt=").append(createdAt)
                .append(", updatedAt=").append(updatedAt)
                .append('}');
        return sb.toString();
    }

    // Factory Pattern
    public static class UserFactory {
        public static User createOAuthUser(String oauthProvider, String oauthId, String name, String email, String profilePicture, Set<String> roles) {
            return new User(null, oauthId, oauthProvider, name, email, null, profilePicture, roles, Instant.now(), Instant.now());
        }

        public static User createLocalUser(String name, String email, String passwordHash, Set<String> roles) {
            return new User(null, null, null, name, email, passwordHash, null, roles, Instant.now(), Instant.now());
        }
    }
}
