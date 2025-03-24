package com.shoply.shoply_backend.services;

import com.shoply.shoply_backend.models.BarcodeScan;
import com.shoply.shoply_backend.repositories.BarcodeScanRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BarcodeScanService {

    private BarcodeScanRepository barcodeScanRepository;

    public BarcodeScanService() {
    }

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

    public List<BarcodeScan> getScansByBarcode(String barcode) {
        return barcodeScanRepository.findByBarcode(barcode);
    }

    public BarcodeScan createScan(BarcodeScan barcodeScan) {
        return barcodeScanRepository.save(barcodeScan);
    }

    public void deleteScan(String id) {
        barcodeScanRepository.deleteById(id);
    }
}


