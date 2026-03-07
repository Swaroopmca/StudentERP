package com.erp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "marks")
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private Double marksObtained;
    private Double maxMarks;
    private LocalDate examDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "entered_by")
    private Teacher enteredBy;

    public enum ExamType { MIDTERM, FINAL, ASSIGNMENT }
}