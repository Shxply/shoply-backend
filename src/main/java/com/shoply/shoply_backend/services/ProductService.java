package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.Product;
import com.shoply.shoply_backend.repositories.ProductRepository;
import com.shoply.shoply_backend.utilities.ChatGPTAPI;
import com.shoply.shoply_backend.utilities.OpenFoodFactsAPI;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public Optional<Product> createProductByBarcode(String barcode) {
        Optional<Product> existing = productRepository.findByBarcode(barcode);
        if (existing.isPresent()) {
            return existing;
        }

        Product product = OpenFoodFactsAPI.getProductByBarcodeMapped(barcode);

        if (product != null) {
            productRepository.save(product);
            return Optional.of(product);
        } else {
            return Optional.empty();
        }
    }

    public String compareProductWithAI(Product product1, Product product2) {
        return ChatGPTAPI.compareProducts(product1, product2);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}


