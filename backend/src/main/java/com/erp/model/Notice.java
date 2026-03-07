package com.erp.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private TargetRole targetRole;
    private LocalDateTime postedAt;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;  // admin or teacher

    public enum TargetRole { ALL, STUDENT, TEACHER }
}