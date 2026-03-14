package com.erp.controller;

import com.erp.model.*;
import com.erp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private FeesService feesService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", (long) studentService.getAllStudents().size());
        stats.put("totalTeachers", (long) teacherService.getAllTeachers().size());
        stats.put("totalCourses", (long) courseService.getAllCourses().size());
        stats.put("totalSubjects", (long) subjectService.getAllSubjects().size());
        stats.put("pendingFees", (long) feesService.getUnpaidFees().size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/reports/students-by-course")
    public ResponseEntity<Map<String, Long>> getStudentsByCourse() {
        List<Course> courses = courseService.getAllCourses();
        Map<String, Long> report = new HashMap<>();
        for (Course course : courses) {
            long count = studentService.getStudentsByCourse(course.getId()).size();
            report.put(course.getName(), count);
        }
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/fees-collection")
    public ResponseEntity<Map<String, Double>> getFeesCollection() {
        List<Fees> allFees = feesService.getAllFees();
        Map<String, Double> report = new HashMap<>();
        double totalCollected = 0;
        double totalPending = 0;
        
        for (Fees fee : allFees) {
            if (fee.getStatus() == Fees.Status.PAID) {
                totalCollected += fee.getAmount();
            } else {
                totalPending += fee.getAmount();
            }
        }
        
        report.put("totalCollected", totalCollected);
        report.put("totalPending", totalPending);
        return ResponseEntity.ok(report);
    }
}