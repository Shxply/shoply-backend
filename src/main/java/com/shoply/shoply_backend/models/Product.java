package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "products")
public class Product {

    @Id
    private String productId;
    private String barcode;
    private Double price;
    private String name;
    private String brand;
    private String brandOwner;
    private String category;
    private String ingredients;
    private String nutriScore;
    private Double energyKcal;
    private Double salt;
    private Double sugar;

    // Images
    private String imageUrl;
    private String imageFrontUrl;
    private String imageIngredientsUrl;
    private String imageNutritionUrl;

    private List<String> ingredientTags;

    public Product() {}

    public Product(String barcode, Double price, String name, String brand, String brandOwner,
                   String category, String ingredients, String nutriScore,
                   Double energyKcal, Double salt, Double sugar,
                   String imageUrl, String imageFrontUrl, String imageIngredientsUrl, String imageNutritionUrl,
                   List<String> ingredientTags) {
        this.barcode = barcode;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.brandOwner = brandOwner;
        this.category = category;
        this.ingredients = ingredients;
        this.nutriScore = nutriScore;
        this.energyKcal = energyKcal;
        this.salt = salt;
        this.sugar = sugar;
        this.imageUrl = imageUrl;
        this.imageFrontUrl = imageFrontUrl;
        this.imageIngredientsUrl = imageIngredientsUrl;
        this.imageNutritionUrl = imageNutritionUrl;
        this.ingredientTags = ingredientTags;
    }

    public String getProductId() {
        return productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getBrandOwner() {
        return brandOwner;
    }

    public String getCategory() {
        return category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getNutriScore() {
        return nutriScore;
    }

    public Double getEnergyKcal() {
        return energyKcal;
    }

    public Double getSalt() {
        return salt;
    }

    public Double getSugar() {
        return sugar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageFrontUrl() {
        return imageFrontUrl;
    }

    public String getImageIngredientsUrl() {
        return imageIngredientsUrl;
    }

    public String getImageNutritionUrl() {
        return imageNutritionUrl;
    }

    public List<String> getIngredientTags() {
        return ingredientTags;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBrandOwner(String brandOwner) {
        this.brandOwner = brandOwner;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setNutriScore(String nutriScore) {
        this.nutriScore = nutriScore;
    }

    public void setEnergyKcal(Double energyKcal) {
        this.energyKcal = energyKcal;
    }

    public void setSalt(Double salt) {
        this.salt = salt;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageFrontUrl(String imageFrontUrl) {
        this.imageFrontUrl = imageFrontUrl;
    }

    public void setImageIngredientsUrl(String imageIngredientsUrl) {
        this.imageIngredientsUrl = imageIngredientsUrl;
    }

    public void setImageNutritionUrl(String imageNutritionUrl) {
        this.imageNutritionUrl = imageNutritionUrl;
    }

    public void setIngredientTags(List<String> ingredientTags) {
        this.ingredientTags = ingredientTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(barcode, product.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", brandOwner='" + brandOwner + '\'' +
                ", category='" + category + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", nutriScore='" + nutriScore + '\'' +
                ", energyKcal=" + energyKcal +
                ", salt=" + salt +
                ", sugar=" + sugar +
                ", imageUrl='" + imageUrl + '\'' +
                ", ingredientTags=" + ingredientTags +
                '}';
    }

    public static class ProductFactory {
        public static Product create(String barcode, String name, String brand, String brandOwner,
                                     String category, String ingredients, String nutriScore,
                                     Double energyKcal, Double salt, Double sugar,
                                     String imageUrl, String imageFrontUrl, String imageIngredientsUrl, String imageNutritionUrl,
                                     List<String> ingredientTags) {
            return new Product(barcode, null, name, brand, brandOwner, category, ingredients, nutriScore,
                    energyKcal, salt, sugar, imageUrl, imageFrontUrl, imageIngredientsUrl, imageNutritionUrl, ingredientTags);
        }
    }
}
