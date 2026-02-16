package com.salesianostriana.seguridad.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthor(String author);

}
