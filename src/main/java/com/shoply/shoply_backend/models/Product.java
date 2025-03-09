package com.shoply.shoply_backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "products")
public class Product {

    @Id
    private String productId;
    private String barcode;
    private String name;
    private String brand;
    private String category;

    public Product() {
    }

    public Product(String barcode, String name, String brand, String category) {
        this.barcode = barcode;
        this.name = name;
        this.brand = brand;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) &&
                Objects.equals(barcode, product.barcode) &&
                Objects.equals(name, product.name) &&
                Objects.equals(brand, product.brand) &&
                Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, barcode, name, brand, category);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{")
                .append("productId='").append(productId).append('\'')
                .append(", barcode='").append(barcode).append('\'')
                .append(", name='").append(name).append('\'')
                .append(", brand='").append(brand).append('\'')
                .append(", category='").append(category).append('\'')
                .append('}');
        return sb.toString();
    }


    // Factory Pattern
    public static class ProductFactory {
        public static Product create(String barcode, String name, String brand, String category) {
            return new Product(barcode, name, brand, category);
        }
    }
}

