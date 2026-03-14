package com.erp.controller;

import com.erp.model.Attendance;
import com.erp.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        if (attendance == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getAttendanceByStudent(@PathVariable Long studentId) {
        return attendanceService.getAttendanceByStudent(studentId);
    }

    @GetMapping("/student/me")
    public ResponseEntity<List<Attendance>> getMyAttendance(@SessionAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(userId));
    }

    @GetMapping("/subject/{subjectId}/date/{date}")
    public List<Attendance> getAttendanceBySubjectAndDate(
            @PathVariable Long subjectId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.getAttendanceBySubjectAndDate(subjectId, date);
    }

    @PostMapping
    public Attendance createAttendance(@RequestBody Attendance attendance) {
        return attendanceService.saveAttendance(attendance);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Attendance>> createBulkAttendance(@RequestBody List<Attendance> attendanceList) {
        List<Attendance> saved = attendanceService.saveAllAttendance(attendanceList);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        Attendance existingAttendance = attendanceService.getAttendanceById(id);
        if (existingAttendance == null) {
            return ResponseEntity.notFound().build();
        }
        attendance.setId(id);
        return ResponseEntity.ok(attendanceService.saveAttendance(attendance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        Attendance existingAttendance = attendanceService.getAttendanceById(id);
        if (existingAttendance == null) {
            return ResponseEntity.notFound().build();
        }
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}