package com.erp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FeeType feeType;
    private Double amount;
    private LocalDate dueDate;
    private LocalDate paidDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public enum FeeType { TUITION, HOSTEL, TRANSPORT, LIBRARY, OTHER }
    public enum Status { PAID, UNPAID, PARTIAL }
}