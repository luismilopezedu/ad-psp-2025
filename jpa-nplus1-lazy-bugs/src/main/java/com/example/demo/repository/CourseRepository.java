package com.example.demo.repository;

import com.example.demo.domain.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // A propósito: no hay métodos optimizados aún (@EntityGraph / join fetch).
    // Se añadirán como "solución" en clase.

    @Query("""
           select c from Course c
              left join fetch c.students
        """)
    List<Course> findAllWithAlumnos();

    @Query("""
           select c from Course c
              left join fetch c.students
              where c.id = :id
        """)
    Optional<Course> findByIdWithStudents(Long id);

    //@EntityGraph(value = "Course.students", type = EntityGraph.EntityGraphType.LOAD)
    @EntityGraph(attributePaths = {"students"})
    @Query("""
            select c from Course c
            """)
    List<Course> findAllEntityGraph();

    @EntityGraph(attributePaths = {"students"})
    List<Course> findByNameIgnoreCase(String name);

}
