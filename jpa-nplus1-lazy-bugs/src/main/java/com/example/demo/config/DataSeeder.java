package com.example.demo.config;

import com.example.demo.domain.Course;
import com.example.demo.domain.Student;
import com.example.demo.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(CourseRepository courseRepository) {
        return args -> {
            // Insertamos datos suficientes para que el N+1 sea evidente en logs.
            int courses = 20;
            int studentsPerCourse = 30;

            for (int i = 1; i <= courses; i++) {
                Course c = new Course("Curso " + i);

                for (int j = 1; j <= studentsPerCourse; j++) {
                    c.addStudent(new Student("Alumno " + i + "-" + j));
                }

                courseRepository.save(c);
            }
        };
    }
}
