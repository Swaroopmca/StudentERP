package com.erp.service;

import com.erp.model.Attendance;
import com.erp.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public List<Attendance> saveAllAttendance(List<Attendance> attendanceList) {
        return attendanceRepository.saveAll(attendanceList);
    }

    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceBySubjectAndDate(Long subjectId, LocalDate date) {
        return attendanceRepository.findBySubjectIdAndDate(subjectId, date);
    }

    public List<Attendance> getAttendanceByStudentAndSubject(Long studentId, Long subjectId) {
        return attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    public double getAttendancePercentage(Long studentId, Long subjectId) {
        List<Attendance> attendances = attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId);
        if (attendances.isEmpty()) return 0.0;
        
        long total = attendances.size();
        long present = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.Status.PRESENT || a.getStatus() == Attendance.Status.LATE)
                .count();
        
        return (present * 100.0) / total;
    }

    public Attendance updateAttendance(Long id, Attendance.Status status) {
        Attendance attendance = getAttendanceById(id);
        if (attendance != null) {
            attendance.setStatus(status);
            return attendanceRepository.save(attendance);
        }
        return null;
    }
}