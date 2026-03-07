package com.erp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
    @Id
    private Long id;  // same as user id
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
    private String address;
    private LocalDate enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    // getters/setters
}