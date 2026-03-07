-- database/schema.sql
CREATE DATABASE IF NOT EXISTS student_erp;
USE student_erp;

-- Users (common for login)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN','TEACHER','STUDENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students
CREATE TABLE students (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    dob DATE,
    address TEXT,
    enrollment_date DATE,
    course_id BIGINT,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Teachers
CREATE TABLE teachers (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    hire_date DATE,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Courses
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE,
    description TEXT,
    duration_months INT
);

-- Subjects
CREATE TABLE subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE,
    course_id BIGINT,
    teacher_id BIGINT,
    credits INT,
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

-- Attendance
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status ENUM('PRESENT','ABSENT','LATE') NOT NULL,
    marked_by BIGINT, -- teacher id
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
    FOREIGN KEY (marked_by) REFERENCES teachers(id),
    UNIQUE KEY unique_attendance (student_id, subject_id, date)
);

-- Marks
CREATE TABLE marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    exam_type ENUM('MIDTERM','FINAL','ASSIGNMENT') NOT NULL,
    marks_obtained DOUBLE,
    max_marks DOUBLE,
    exam_date DATE,
    entered_by BIGINT, -- teacher id
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
    FOREIGN KEY (entered_by) REFERENCES teachers(id)
);

-- Fees
CREATE TABLE fees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    fee_type ENUM('TUITION','HOSTEL','TRANSPORT','LIBRARY','OTHER') NOT NULL,
    amount DOUBLE NOT NULL,
    due_date DATE,
    paid_date DATE,
    status ENUM('PAID','UNPAID','PARTIAL') DEFAULT 'UNPAID',
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Notices
CREATE TABLE notices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    posted_by BIGINT, -- admin/teacher id
    target_role ENUM('ALL','STUDENT','TEACHER') DEFAULT 'ALL',
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);