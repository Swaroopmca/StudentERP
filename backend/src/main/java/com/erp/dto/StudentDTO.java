package com.erp.dto;

import com.erp.model.Student;
import java.time.LocalDate;

public class StudentDTO {
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
    private String address;
    private LocalDate enrollmentDate;
    private Long courseId;
    private String courseName;
    private String username;

    public StudentDTO() {}

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.fullName = student.getFirstName() + " " + student.getLastName();
        this.email = student.getEmail();
        this.phone = student.getPhone();
        this.dob = student.getDob();
        this.address = student.getAddress();
        this.enrollmentDate = student.getEnrollmentDate();
        if (student.getCourse() != null) {
            this.courseId = student.getCourse().getId();
            this.courseName = student.getCourse().getName();
        }
        if (student.getUser() != null) {
            this.username = student.getUser().getUsername();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}