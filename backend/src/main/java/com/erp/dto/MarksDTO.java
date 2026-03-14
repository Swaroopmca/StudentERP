package com.erp.dto;

import com.erp.model.Marks;
import java.time.LocalDate;

public class MarksDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String examType;
    private Double marksObtained;
    private Double maxMarks;
    private LocalDate examDate;
    private Double percentage;

    public MarksDTO() {}

    public MarksDTO(Marks marks) {
        this.id = marks.getId();
        if (marks.getStudent() != null) {
            this.studentId = marks.getStudent().getId();
            this.studentName = marks.getStudent().getFirstName() + " " + marks.getStudent().getLastName();
        }
        if (marks.getSubject() != null) {
            this.subjectId = marks.getSubject().getId();
            this.subjectName = marks.getSubject().getName();
        }
        this.examType = marks.getExamType() != null ? marks.getExamType().toString() : null;
        this.marksObtained = marks.getMarksObtained();
        this.maxMarks = marks.getMaxMarks();
        this.examDate = marks.getExamDate();
        if (marks.getMarksObtained() != null && marks.getMaxMarks() != null && marks.getMaxMarks() > 0) {
            this.percentage = (marks.getMarksObtained() / marks.getMaxMarks()) * 100;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    
    public Double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Double marksObtained) { this.marksObtained = marksObtained; }
    
    public Double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Double maxMarks) { this.maxMarks = maxMarks; }
    
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}