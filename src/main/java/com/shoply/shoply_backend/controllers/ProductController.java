package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.models.Product;
import com.shoply.shoply_backend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/barcode/{barcode}")
    public Optional<Product> getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode);
    }

    @GetMapping("/compare/{barcode}")
    public ResponseEntity<Product> compareProductByBarcode(@PathVariable String barcode) {
        return productService.createProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/compare/ai")
    public ResponseEntity<String> compareProductsWithAI(@RequestBody List<Product> products) {

        Product product1 = products.get(0);
        Product product2 = products.get(1);

        String result = productService.compareProductWithAI(product1, product2);
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}

