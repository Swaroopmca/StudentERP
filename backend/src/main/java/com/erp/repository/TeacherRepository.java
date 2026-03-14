package com.erp.repository;

import com.erp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t WHERE t.email = ?1")
    Teacher findByEmail(String email);
    
    @Query("SELECT t FROM Teacher t WHERE t.firstName LIKE %?1% OR t.lastName LIKE %?1%")
    List<Teacher> searchByName(String name);
}