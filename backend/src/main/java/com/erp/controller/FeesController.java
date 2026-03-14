package com.erp.controller;

import com.erp.model.Fees;
import com.erp.service.FeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "*")
public class FeesController {
    @Autowired
    private FeesService feesService;

    @GetMapping
    public List<Fees> getAllFees() {
        return feesService.getAllFees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fees> getFeesById(@PathVariable Long id) {
        Fees fees = feesService.getFeesById(id);
        if (fees == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/student/{studentId}")
    public List<Fees> getFeesByStudent(@PathVariable Long studentId) {
        return feesService.getFeesByStudent(studentId);
    }

    @GetMapping("/student/me")
    public ResponseEntity<List<Fees>> getMyFees(@SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(feesService.getFeesByStudent(userId));
    }

    @GetMapping("/unpaid")
    public List<Fees> getUnpaidFees() {
        return feesService.getUnpaidFees();
    }

    @PostMapping
    public Fees createFees(@RequestBody Fees fees) {
        return feesService.saveFees(fees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fees> updateFees(@PathVariable Long id, @RequestBody Fees fees) {
        Fees existingFees = feesService.getFeesById(id);
        if (existingFees == null) {
            return ResponseEntity.notFound().build();
        }
        fees.setId(id);
        return ResponseEntity.ok(feesService.saveFees(fees));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Fees> payFees(@PathVariable Long id) {
        Fees fees = feesService.getFeesById(id);
        if (fees == null) {
            return ResponseEntity.notFound().build();
        }
        fees.setStatus(Fees.Status.PAID);
        fees.setPaidDate(java.time.LocalDate.now());
        return ResponseEntity.ok(feesService.saveFees(fees));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFees(@PathVariable Long id) {
        Fees existingFees = feesService.getFeesById(id);
        if (existingFees == null) {
            return ResponseEntity.notFound().build();
        }
        feesService.deleteFees(id);
        return ResponseEntity.noContent().build();
    }
}