package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "shopping_lists")
public class ShoppingList {

    @Id
    private String shoppingListId;
    private String userId;
    private Instant createdAt;
    private boolean active;
    private boolean generatedByAI;

    public ShoppingList() {
    }

    public ShoppingList(String shoppingListId, String userId, Instant createdAt, boolean active, boolean generatedByAI) {
        this.shoppingListId = shoppingListId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.active = active;
        this.generatedByAI = generatedByAI;
    }

    // Getters and setters
    public String getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isGeneratedByAI() {
        return generatedByAI;
    }

    public void setGeneratedByAI(boolean generatedByAI) {
        this.generatedByAI = generatedByAI;
    }

    public void complete() {
        this.active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return active == that.active &&
                generatedByAI == that.generatedByAI &&
                Objects.equals(shoppingListId, that.shoppingListId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingListId, userId, createdAt, active, generatedByAI);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShoppingList{")
                .append("shoppingListId='").append(shoppingListId).append('\'')
                .append(", userId='").append(userId).append('\'')
                .append(", createdAt=").append(createdAt)
                .append(", active=").append(active)
                .append(", generatedByAI=").append(generatedByAI)
                .append('}');
        return sb.toString();
    }

    // Factory Pattern
    public static class ShoppingListFactory {
        public static ShoppingList createUserGeneratedList(String userId, boolean active) {
            return new ShoppingList(null, userId, Instant.now(), active, false);
        }

        public static ShoppingList createAIGeneratedList(String userId, boolean active) {
            return new ShoppingList(null, userId, Instant.now(), active, true);
        }
    }
}



