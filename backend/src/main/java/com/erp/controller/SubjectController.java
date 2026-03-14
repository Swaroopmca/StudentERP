package com.erp.controller;

import com.erp.model.Subject;
import com.erp.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<Subject> getSubjectsByTeacher(@PathVariable Long teacherId) {
        return subjectService.getSubjectsByTeacher(teacherId);
    }

    @GetMapping("/course/{courseId}")
    public List<Subject> getSubjectsByCourse(@PathVariable Long courseId) {
        return subjectService.getSubjectsByCourse(courseId);
    }

    @GetMapping("/teacher/me")
    public ResponseEntity<List<Subject>> getMySubjects(@SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(subjectService.getSubjectsByTeacher(userId));
    }

    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectService.saveSubject(subject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject subject) {
        Subject existingSubject = subjectService.getSubjectById(id);
        if (existingSubject == null) {
            return ResponseEntity.notFound().build();
        }
        subject.setId(id);
        return ResponseEntity.ok(subjectService.saveSubject(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        Subject existingSubject = subjectService.getSubjectById(id);
        if (existingSubject == null) {
            return ResponseEntity.notFound().build();
        }
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}