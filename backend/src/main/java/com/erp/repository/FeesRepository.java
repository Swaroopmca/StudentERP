package com.erp.repository;

import com.erp.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {
    List<Fees> findByStudentId(Long studentId);
    
    List<Fees> findByStatus(Fees.Status status);
    
    @Query("SELECT f FROM Fees f WHERE f.dueDate < :currentDate AND f.status = 'UNPAID'")
    List<Fees> findOverdueFees(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT SUM(f.amount) FROM Fees f WHERE f.status = 'PAID'")
    Double getTotalCollected();
    
    @Query("SELECT SUM(f.amount) FROM Fees f WHERE f.status = 'UNPAID'")
    Double getTotalPending();
    
    @Query("SELECT f FROM Fees f WHERE f.student.id = :studentId AND f.status = 'UNPAID'")
    List<Fees> findUnpaidByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT f FROM Fees f WHERE f.paidDate BETWEEN :startDate AND :endDate")
    List<Fees> findByPaidDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}