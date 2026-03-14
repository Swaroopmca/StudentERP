package com.erp.repository;

import com.erp.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudentId(Long studentId);
    
    List<Marks> findBySubjectId(Long subjectId);
    
    List<Marks> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    @Query("SELECT m FROM Marks m WHERE m.student.id = :studentId AND m.subject.id = :subjectId AND m.examType = :examType")
    Marks findByStudentAndSubjectAndExamType(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("examType") Marks.ExamType examType);
    
    @Query("SELECT AVG(m.marksObtained) FROM Marks m WHERE m.subject.id = :subjectId")
    Double getAverageMarksBySubject(@Param("subjectId") Long subjectId);
    
    @Query("SELECT MAX(m.marksObtained) FROM Marks m WHERE m.subject.id = :subjectId")
    Double getHighestMarksBySubject(@Param("subjectId") Long subjectId);
    
    @Query("SELECT MIN(m.marksObtained) FROM Marks m WHERE m.subject.id = :subjectId")
    Double getLowestMarksBySubject(@Param("subjectId") Long subjectId);
}