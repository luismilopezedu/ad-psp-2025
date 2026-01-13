package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // TRAMPA: LAZY en ManyToOne también, aunque aquí no es el foco
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    protected Student() {
        // JPA
    }

    public Student(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }

    void setCourse(Course course) {
        this.course = course;
    }
}
