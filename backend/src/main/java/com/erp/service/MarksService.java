package com.erp.service;

import com.erp.model.Marks;
import com.erp.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarksService {
    @Autowired
    private MarksRepository marksRepository;

    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }

    public Marks getMarksById(Long id) {
        return marksRepository.findById(id).orElse(null);
    }

    public Marks saveMarks(Marks marks) {
        return marksRepository.save(marks);
    }

    @Transactional
    public List<Marks> saveAllMarks(List<Marks> marksList) {
        return marksRepository.saveAll(marksList);
    }

    public void deleteMarks(Long id) {
        marksRepository.deleteById(id);
    }

    public List<Marks> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentId(studentId);
    }

    public List<Marks> getMarksBySubject(Long subjectId) {
        return marksRepository.findBySubjectId(subjectId);
    }

    public List<Marks> getMarksByStudentAndSubject(Long studentId, Long subjectId) {
        return marksRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }

    public Double getAverageMarksBySubject(Long subjectId) {
        List<Marks> marksList = marksRepository.findBySubjectId(subjectId);
        if (marksList.isEmpty()) return 0.0;
        
        return marksList.stream()
                .mapToDouble(Marks::getMarksObtained)
                .average()
                .orElse(0.0);
    }

    public Marks updateMarks(Long id, Double marksObtained) {
        Marks marks = getMarksById(id);
        if (marks != null) {
            marks.setMarksObtained(marksObtained);
            return marksRepository.save(marks);
        }
        return null;
    }
}