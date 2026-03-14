package com.erp.service;

import com.erp.model.Course;
import com.erp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        if (course != null) {
            course.setName(courseDetails.getName());
            course.setCode(courseDetails.getCode());
            course.setDescription(courseDetails.getDescription());
            course.setDurationMonths(courseDetails.getDurationMonths());
            return courseRepository.save(course);
        }
        return null;
    }

    public Course findByCode(String code) {
        return courseRepository.findByCode(code);
    }
}