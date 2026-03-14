package com.erp.repository;

import com.erp.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    
    List<Attendance> findBySubjectIdAndDate(Long subjectId, LocalDate date);
    
    List<Attendance> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.date BETWEEN :startDate AND :endDate")
    List<Attendance> findByStudentIdAndDateRange(
            @Param("studentId") Long studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.subject.id = :subjectId AND a.status IN ('PRESENT', 'LATE')")
    long countPresentByStudentAndSubject(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.subject.id = :subjectId")
    long countTotalByStudentAndSubject(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId);
}