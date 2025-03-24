package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.annotations.ExpectedResult;
import com.shoply.shoply_backend.annotations.IntegrationTest;
import com.shoply.shoply_backend.annotations.MockDependency;
import com.shoply.shoply_backend.annotations.TestableService;
import com.shoply.shoply_backend.models.Product;
import com.shoply.shoply_backend.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@TestableService
@Service
public class ProductService {

    @MockDependency
    private ProductRepository productRepository;

    public ProductService() {
    }

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "[]", expectedJson = "[]")  // No products initially
    @ExpectedResult(inputJson = "[\"Product1\", \"Product2\"]", expectedJson = "[{\"id\":\"1\",\"name\":\"Product1\",\"barcode\":\"12345\"}, {\"id\":\"2\",\"name\":\"Product2\",\"barcode\":\"67890\"}]")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "{\"id\":\"1\",\"name\":\"Product1\",\"barcode\":\"12345\"}")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__NULL__")
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"12345\"", expectedJson = "{\"id\":\"1\",\"name\":\"Product1\",\"barcode\":\"12345\"}")
    @ExpectedResult(inputJson = "\"67890\"", expectedJson = "__NULL__")
    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "{\"name\":\"New Product\",\"barcode\":\"54321\"}", expectedJson = "{\"id\":\"3\",\"name\":\"New Product\",\"barcode\":\"54321\"}")
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "__VOID__")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__VOID__")
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}


