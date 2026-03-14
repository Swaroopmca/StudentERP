package com.erp.service;

import com.erp.model.Subject;
import com.erp.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = getSubjectById(id);
        if (subject != null) {
            subject.setName(subjectDetails.getName());
            subject.setCode(subjectDetails.getCode());
            subject.setCredits(subjectDetails.getCredits());
            subject.setCourse(subjectDetails.getCourse());
            subject.setTeacher(subjectDetails.getTeacher());
            return subjectRepository.save(subject);
        }
        return null;
    }

    public List<Subject> getSubjectsByCourse(Long courseId) {
        return subjectRepository.findByCourseId(courseId);
    }

    public List<Subject> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findByTeacherId(teacherId);
    }

    public Subject findByCode(String code) {
        return subjectRepository.findByCode(code);
    }
}