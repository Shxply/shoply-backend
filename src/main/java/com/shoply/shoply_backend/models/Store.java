package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Document(collection = "stores")
public class Store {

    @Id
    private String storeId;
    private String name;
    private String businessStatus;
    private List<String> types;
    private String vicinity;
    private Double rating;
    private Integer userRatingsTotal;
    private Integer priceLevel;
    private String placeId;

    private String photoReference;
    private List<String> photoAttributions;

    @GeoSpatialIndexed
    private GeoJsonPoint location;

    private Boolean openNow;

    private Instant createdAt;
    private Instant updatedAt;

    public Store() {}

    public Store(String storeId, String name, String businessStatus, List<String> types, String vicinity,
                 Double rating, Integer userRatingsTotal, Integer priceLevel, String placeId,
                 String photoReference, List<String> photoAttributions,
                 double latitude, double longitude, Boolean openNow,
                 Instant createdAt, Instant updatedAt) {
        this.storeId = storeId;
        this.name = name;
        this.businessStatus = businessStatus;
        this.types = types;
        this.vicinity = vicinity;
        this.rating = rating;
        this.userRatingsTotal = userRatingsTotal;
        this.priceLevel = priceLevel;
        this.placeId = placeId;
        this.photoReference = photoReference;
        this.photoAttributions = photoAttributions;
        this.location = new GeoJsonPoint(longitude, latitude);
        this.openNow = openNow;
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

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public List<String> getPhotoAttributions() {
        return photoAttributions;
    }

    public void setPhotoAttributions(List<String> photoAttributions) {
        this.photoAttributions = photoAttributions;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store store)) return false;
        return Objects.equals(storeId, store.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId);
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId='" + storeId + '\'' +
                ", name='" + name + '\'' +
                ", businessStatus='" + businessStatus + '\'' +
                ", types=" + types +
                ", vicinity='" + vicinity + '\'' +
                ", rating=" + rating +
                ", userRatingsTotal=" + userRatingsTotal +
                ", priceLevel=" + priceLevel +
                ", placeId='" + placeId + '\'' +
                ", photoReference='" + photoReference + '\'' +
                ", photoAttributions=" + photoAttributions +
                ", location=" + location +
                ", openNow=" + openNow +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Factory Pattern for convenience
    public static class StoreFactory {
        public static Store create(String name, String businessStatus, List<String> types, String vicinity,
                                   Double rating, Integer userRatingsTotal, Integer priceLevel, String placeId,
                                   String photoReference, List<String> photoAttributions,
                                   double latitude, double longitude, Boolean openNow) {
            Instant now = Instant.now();
            return new Store(null, name, businessStatus, types, vicinity, rating, userRatingsTotal, priceLevel,
                    placeId, photoReference, photoAttributions,
                    latitude, longitude, openNow, now, now);
        }
    }
}
