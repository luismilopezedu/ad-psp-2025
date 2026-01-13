package com.example.demo.web.dto;

import java.util.List;

public record CourseDetailsDto(Long id, String name, List<StudentDto> students) {

    public record StudentDto(Long id, String name) {
    }
}
