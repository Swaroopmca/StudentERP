package com.erp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}