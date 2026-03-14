package com.erp.controller;

import com.erp.model.Marks;
import com.erp.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "*")
public class MarksController {
    @Autowired
    private MarksService marksService;

    @GetMapping
    public List<Marks> getAllMarks() {
        return marksService.getAllMarks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marks> getMarksById(@PathVariable Long id) {
        Marks marks = marksService.getMarksById(id);
        if (marks == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/student/{studentId}")
    public List<Marks> getMarksByStudent(@PathVariable Long studentId) {
        return marksService.getMarksByStudent(studentId);
    }

    @GetMapping("/student/me")
    public ResponseEntity<List<Marks>> getMyMarks(@SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(marksService.getMarksByStudent(userId));
    }

    @GetMapping("/subject/{subjectId}")
    public List<Marks> getMarksBySubject(@PathVariable Long subjectId) {
        return marksService.getMarksBySubject(subjectId);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public List<Marks> getMarksByStudentAndSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        return marksService.getMarksByStudentAndSubject(studentId, subjectId);
    }

    @PostMapping
    public Marks createMarks(@RequestBody Marks marks) {
        return marksService.saveMarks(marks);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Marks>> createBulkMarks(@RequestBody List<Marks> marksList) {
        List<Marks> saved = marksService.saveAllMarks(marksList);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marks> updateMarks(@PathVariable Long id, @RequestBody Marks marks) {
        Marks existingMarks = marksService.getMarksById(id);
        if (existingMarks == null) {
            return ResponseEntity.notFound().build();
        }
        marks.setId(id);
        return ResponseEntity.ok(marksService.saveMarks(marks));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable Long id) {
        Marks existingMarks = marksService.getMarksById(id);
        if (existingMarks == null) {
            return ResponseEntity.notFound().build();
        }
        marksService.deleteMarks(id);
        return ResponseEntity.noContent().build();
    }
}