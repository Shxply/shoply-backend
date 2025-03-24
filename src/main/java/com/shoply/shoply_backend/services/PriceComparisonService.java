package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.annotations.ExpectedResult;
import com.shoply.shoply_backend.annotations.IntegrationTest;
import com.shoply.shoply_backend.annotations.MockDependency;
import com.shoply.shoply_backend.annotations.TestableService;
import com.shoply.shoply_backend.models.PriceComparison;
import com.shoply.shoply_backend.repositories.PriceComparisonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@TestableService
@Service
public class PriceComparisonService {

    @MockDependency
    private PriceComparisonRepository priceComparisonRepository;

    public PriceComparisonService() {
    }

    public PriceComparisonService(PriceComparisonRepository priceComparisonRepository) {
        this.priceComparisonRepository = priceComparisonRepository;
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "[]", expectedJson = "[]")  // No comparisons initially
    @ExpectedResult(inputJson = "[\"Comparison1\", \"Comparison2\"]", expectedJson = "[{\"id\":\"1\",\"productId\":\"prod1\",\"storeId\":\"store1\",\"price\":100.00}, {\"id\":\"2\",\"productId\":\"prod2\",\"storeId\":\"store2\",\"price\":150.00}]")
    public List<PriceComparison> getAllComparisons() {
        return priceComparisonRepository.findAll();
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "{\"id\":\"1\",\"productId\":\"prod1\",\"storeId\":\"store1\",\"price\":100.00}")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__NULL__")
    public Optional<PriceComparison> getComparisonById(String id) {
        return priceComparisonRepository.findById(id);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"prod1\"", expectedJson = "[{\"id\":\"1\",\"productId\":\"prod1\",\"storeId\":\"store1\",\"price\":100.00}]")
    @ExpectedResult(inputJson = "\"prod2\"", expectedJson = "[{\"id\":\"2\",\"productId\":\"prod2\",\"storeId\":\"store2\",\"price\":150.00}]")
    public List<PriceComparison> getComparisonsByProductId(String productId) {
        return priceComparisonRepository.findByProductId(productId);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"store1\"", expectedJson = "[{\"id\":\"1\",\"productId\":\"prod1\",\"storeId\":\"store1\",\"price\":100.00}]")
    @ExpectedResult(inputJson = "\"store2\"", expectedJson = "[{\"id\":\"2\",\"productId\":\"prod2\",\"storeId\":\"store2\",\"price\":150.00}]")
    public List<PriceComparison> getComparisonsByStoreId(String storeId) {
        return priceComparisonRepository.findByStoreId(storeId);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "{\"productId\":\"prod3\",\"storeId\":\"store3\",\"price\":200.00}", expectedJson = "{\"id\":\"3\",\"productId\":\"prod3\",\"storeId\":\"store3\",\"price\":200.00}")
    public PriceComparison createComparison(PriceComparison priceComparison) {
        return priceComparisonRepository.save(priceComparison);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "__VOID__")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__VOID__")
    public void deleteComparison(String id) {
        priceComparisonRepository.deleteById(id);
    }
}


