package com.erp.service;

import com.erp.model.Fees;
import com.erp.repository.FeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FeesService {
    @Autowired
    private FeesRepository feesRepository;

    public List<Fees> getAllFees() {
        return feesRepository.findAll();
    }

    public Fees getFeesById(Long id) {
        return feesRepository.findById(id).orElse(null);
    }

    public Fees saveFees(Fees fees) {
        return feesRepository.save(fees);
    }

    public void deleteFees(Long id) {
        feesRepository.deleteById(id);
    }

    public List<Fees> getFeesByStudent(Long studentId) {
        return feesRepository.findByStudentId(studentId);
    }

    public List<Fees> getUnpaidFees() {
        return feesRepository.findByStatus(Fees.Status.UNPAID);
    }

    public List<Fees> getOverdueFees() {
        List<Fees> unpaid = feesRepository.findByStatus(Fees.Status.UNPAID);
        LocalDate now = LocalDate.now();
        return unpaid.stream()
                .filter(fee -> fee.getDueDate() != null && fee.getDueDate().isBefore(now))
                .toList();
    }

    public Fees markAsPaid(Long id) {
        Fees fees = getFeesById(id);
        if (fees != null) {
            fees.setStatus(Fees.Status.PAID);
            fees.setPaidDate(LocalDate.now());
            return feesRepository.save(fees);
        }
        return null;
    }

    public double getTotalFeesCollected() {
        return feesRepository.findByStatus(Fees.Status.PAID).stream()
                .mapToDouble(Fees::getAmount)
                .sum();
    }

    public double getTotalFeesPending() {
        return feesRepository.findByStatus(Fees.Status.UNPAID).stream()
                .mapToDouble(Fees::getAmount)
                .sum();
    }
}