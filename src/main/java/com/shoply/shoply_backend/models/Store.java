package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "stores")
public class Store {

    @Id
    private String storeId;
    private String name;
    @GeoSpatialIndexed
    private GeoJsonPoint location;
    private Instant createdAt;
    private Instant updatedAt;

    public Store() {}

    public Store(String storeId, String name, double latitude, double longitude, Instant createdAt, Instant updatedAt) {
        this.storeId = storeId;
        this.name = name;
        this.location = new GeoJsonPoint(longitude, latitude);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public GeoJsonPoint getLocation() { return location; }
    public void setLocation(double latitude, double longitude) {
        this.location = new GeoJsonPoint(longitude, latitude);
    }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store store)) return false;
        return Objects.equals(getStoreId(), store.getStoreId())
                && Objects.equals(getName(), store.getName())
                && Objects.equals(getLocation(), store.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStoreId(), getName(), getLocation());
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Factory Pattern
    public static class StoreFactory {
        public static Store create(String name, double latitude, double longitude) {
            return new Store(null, name, latitude, longitude, Instant.now(), Instant.now());
        }
    }
}


