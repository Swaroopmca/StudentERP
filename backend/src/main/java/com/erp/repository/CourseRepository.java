package com.erp.repository;

import com.erp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.code = ?1")
    Course findByCode(String code);
    
    @Query("SELECT c FROM Course c WHERE c.name LIKE %?1%")
    Course findByNameContaining(String name);
}