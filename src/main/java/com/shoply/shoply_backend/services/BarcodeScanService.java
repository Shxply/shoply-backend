package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.annotations.ExpectedResult;
import com.shoply.shoply_backend.annotations.IntegrationTest;
import com.shoply.shoply_backend.annotations.MockDependency;
import com.shoply.shoply_backend.annotations.TestableService;
import com.shoply.shoply_backend.models.BarcodeScan;
import com.shoply.shoply_backend.repositories.BarcodeScanRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@TestableService
@Service
public class BarcodeScanService {

    @MockDependency
    private BarcodeScanRepository barcodeScanRepository;

    public BarcodeScanService() {
    }

    public BarcodeScanService(BarcodeScanRepository barcodeScanRepository) {
        this.barcodeScanRepository = barcodeScanRepository;
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "[]", expectedJson = "[]")  // No barcode scans initially
    @ExpectedResult(inputJson = "[\"Scan1\", \"Scan2\"]", expectedJson = "[{\"id\":\"1\",\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}, {\"id\":\"2\",\"userId\":\"user2\",\"barcode\":\"67890\",\"scanTime\":\"2023-04-01T11:00:00\"}]")
    public List<BarcodeScan> getAllScans() {
        return barcodeScanRepository.findAll();
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "{\"id\":\"1\",\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__NULL__")
    public Optional<BarcodeScan> getScanById(String id) {
        return barcodeScanRepository.findById(id);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"user1\"", expectedJson = "[{\"id\":\"1\",\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}]")
    @ExpectedResult(inputJson = "\"user2\"", expectedJson = "[{\"id\":\"2\",\"userId\":\"user2\",\"barcode\":\"67890\",\"scanTime\":\"2023-04-01T11:00:00\"}]")
    public List<BarcodeScan> getScansByUserId(String userId) {
        return barcodeScanRepository.findByUserId(userId);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"12345\"", expectedJson = "[{\"id\":\"1\",\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}]")
    @ExpectedResult(inputJson = "\"67890\"", expectedJson = "[{\"id\":\"2\",\"userId\":\"user2\",\"barcode\":\"67890\",\"scanTime\":\"2023-04-01T11:00:00\"}]")
    public List<BarcodeScan> getScansByBarcode(String barcode) {
        return barcodeScanRepository.findByBarcode(barcode);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "{\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}", expectedJson = "{\"id\":\"1\",\"userId\":\"user1\",\"barcode\":\"12345\",\"scanTime\":\"2023-04-01T10:00:00\"}")
    public BarcodeScan createScan(BarcodeScan barcodeScan) {
        return barcodeScanRepository.save(barcodeScan);
    }

    @IntegrationTest
    @ExpectedResult(inputJson = "\"1\"", expectedJson = "__VOID__")
    @ExpectedResult(inputJson = "\"2\"", expectedJson = "__VOID__")
    public void deleteScan(String id) {
        barcodeScanRepository.deleteById(id);
    }
}


