package com.erp.controller;

import com.erp.model.Teacher;
import com.erp.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/me")
    public ResponseEntity<Teacher> getCurrentTeacher(@SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Teacher teacher = teacherService.getTeacherById(userId);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.saveTeacher(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher existingTeacher = teacherService.getTeacherById(id);
        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        teacher.setId(id);
        return ResponseEntity.ok(teacherService.saveTeacher(teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Teacher existingTeacher = teacherService.getTeacherById(id);
        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}