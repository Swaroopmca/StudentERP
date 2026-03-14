package com.erp.service;

import com.erp.model.*;
import com.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private FeesRepository feesRepository;
    
    @Autowired
    private NoticeRepository noticeRepository;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalStudents", studentRepository.count());
        summary.put("totalTeachers", teacherRepository.count());
        summary.put("totalCourses", courseRepository.count());
        summary.put("totalSubjects", subjectRepository.count());
        summary.put("pendingFees", feesRepository.findByStatus(Fees.Status.UNPAID).size());
        summary.put("recentNotices", noticeRepository.findLatestNotices(5));
        return summary;
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<Fees> getAllFees() {
        return feesRepository.findAll();
    }

    public Map<String, Double> getFeeReport() {
        List<Fees> allFees = feesRepository.findAll();
        Map<String, Double> report = new HashMap<>();
        double collected = 0, pending = 0;
        
        for (Fees fee : allFees) {
            if (fee.getStatus() == Fees.Status.PAID) {
                collected += fee.getAmount();
            } else {
                pending += fee.getAmount();
            }
        }
        
        report.put("collected", collected);
        report.put("pending", pending);
        return report;
    }

    public Map<String, Long> getStudentCourseDistribution() {
        List<Course> courses = courseRepository.findAll();
        Map<String, Long> distribution = new HashMap<>();
        
        for (Course course : courses) {
            long count = studentRepository.findByCourseId(course.getId()).size();
            distribution.put(course.getName(), count);
        }
        
        return distribution;
    }
}