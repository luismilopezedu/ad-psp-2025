package com.example.demo.web;

import com.example.demo.domain.Course;
import com.example.demo.service.CourseService;
import com.example.demo.web.dto.CourseDetailsDto;
import com.example.demo.web.dto.CourseListItemDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ===========================
    // TRAMPA #3: N+1 en listado
    // ===========================
    @GetMapping
    public List<CourseListItemDto> listCourses() {
        List<Course> courses = courseService.getAllCoursesWithAlumnos();

        // Aquí provocamos N+1: al acceder a getStudents().size() (LAZY) por cada curso,
        // Hibernate lanza una query adicional por curso.
        return courses.stream()
                .map(c -> new CourseListItemDto(c.getId(), c.getName(), c.getStudents().size()))
                .toList();
    }

    @GetMapping("/fail")
    public List<CourseListItemDto> listCoursesFail() {
        List<Course> courses = courseService.getAllCourses();

        // Aquí provocamos N+1: al acceder a getStudents().size() (LAZY) por cada curso,
        // Hibernate lanza una query adicional por curso.
        return courses.stream()
                .map(c -> new CourseListItemDto(c.getId(), c.getName(), c.getStudents().size()))
                .toList();
    }

    // =========================================
    // TRAMPA #4: LazyInitializationException
    // =========================================
    @GetMapping("/{id}")
    public CourseDetailsDto getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);

        // Si el service no es transaccional, al iterar students aquí (LAZY)
        // Hibernate suele lanzar LazyInitializationException.
        List<CourseDetailsDto.StudentDto> students = course.getStudents().stream()
                .map(s -> new CourseDetailsDto.StudentDto(s.getId(), s.getName()))
                .toList();

        return new CourseDetailsDto(course.getId(), course.getName(), students);
    }
}
