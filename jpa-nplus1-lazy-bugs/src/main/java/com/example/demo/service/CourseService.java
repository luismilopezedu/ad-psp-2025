package com.example.demo.service;

import com.example.demo.domain.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // TRAMPA #2: no hay @Transactional.
    // Devolvemos entidades y el controller accede a relaciones LAZY fuera de sesión.
    /*public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }*/

    public List<Course> getAllCoursesWithAlumnos() {
        return courseRepository.findAllEntityGraph();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
