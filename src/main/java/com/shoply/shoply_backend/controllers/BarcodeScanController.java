package com.shoply.shoply_backend.controllers;

import com.shoply.shoply_backend.models.BarcodeScan;
import com.shoply.shoply_backend.services.BarcodeScanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/barcode-scans")
public class BarcodeScanController {
    private final BarcodeScanService barcodeScanService;

    public BarcodeScanController(BarcodeScanService barcodeScanService) {
        this.barcodeScanService = barcodeScanService;
    }

    @GetMapping
    public List<BarcodeScan> getAllScans() {
        return barcodeScanService.getAllScans();
    }

    @GetMapping("/{id}")
    public Optional<BarcodeScan> getScanById(@PathVariable String id) {
        return barcodeScanService.getScanById(id);
    }

    @GetMapping("/user/{userId}")
    public List<BarcodeScan> getScansByUserId(@PathVariable String userId) {
        return barcodeScanService.getScansByUserId(userId);
    }

    @PostMapping
    public BarcodeScan createScan(@RequestBody BarcodeScan barcodeScan) {
        return barcodeScanService.createScan(barcodeScan);
    }

    @DeleteMapping("/{id}")
    public void deleteScan(@PathVariable String id) {
        barcodeScanService.deleteScan(id);
    }
}

