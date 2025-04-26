package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.BarcodeScan;
import com.shoply.shoply_backend.models.BarcodeScan.BarcodeScanFactory;
import com.shoply.shoply_backend.repositories.BarcodeScanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BarcodeScanService {

    private BarcodeScanRepository barcodeScanRepository;

    public BarcodeScanService(BarcodeScanRepository barcodeScanRepository) {
        this.barcodeScanRepository = barcodeScanRepository;
    }

    public List<BarcodeScan> getAllScans() {
        return barcodeScanRepository.findAll();
    }

    public Optional<BarcodeScan> getScanById(String id) {
        return barcodeScanRepository.findById(id);
    }

    public List<BarcodeScan> getScansByUserId(String userId) {
        return barcodeScanRepository.findByUserId(userId);
    }

    public BarcodeScan createScan(BarcodeScan barcodeScan) {
        return barcodeScanRepository.save(barcodeScan);
    }

    public BarcodeScan createScanAndUpdateMostRecent(String userId, String storeId, String productId, double scannedPrice) {
        Optional<BarcodeScan> existing = barcodeScanRepository.findTopByProductIdAndStoreIdOrderByScanTimestampDesc(productId, storeId);
        existing.ifPresent(e -> barcodeScanRepository.deleteById(e.getScanId()));
        BarcodeScan newScan = BarcodeScanFactory.create(userId, storeId, productId, scannedPrice);
        return barcodeScanRepository.save(newScan);
    }

    public void deleteScan(String id) {
        barcodeScanRepository.deleteById(id);
    }

    public List<BarcodeScan> getScansByProductId(String productId) {
        return barcodeScanRepository.findByProductId(productId);
    }

}

