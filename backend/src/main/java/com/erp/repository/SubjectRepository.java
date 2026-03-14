package com.erp.repository;

import com.erp.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByCourseId(Long courseId);
    
    List<Subject> findByTeacherId(Long teacherId);
    
    @Query("SELECT s FROM Subject s WHERE s.code = ?1")
    Subject findByCode(String code);
    
    @Query("SELECT s FROM Subject s WHERE s.course.id = ?1 AND s.teacher.id = ?2")
    List<Subject> findByCourseIdAndTeacherId(Long courseId, Long teacherId);
}