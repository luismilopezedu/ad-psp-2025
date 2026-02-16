package com.salesianostriana.seguridad.business;

import com.salesianostriana.seguridad.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.status(201)
                .body(taskRepository.save(task));
    }

    @GetMapping("/mine")
    public List<Task> findMyTasks(@AuthenticationPrincipal User me) {
        return taskRepository.findByAuthor(me.getId().toString())
                .stream()
                .toList();
    }

    @PostAuthorize("""
            returnObject.author == authentication.principal.getId().toString() or hasRole('ADMIN')
            """)
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

}
