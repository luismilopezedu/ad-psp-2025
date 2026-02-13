package com.salesianostriana.seguridad;

import com.salesianostriana.seguridad.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping
    public String endpoint() {
        return "Hello World!";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal User user) {
        return "Hello %s!".formatted(user.getUsername());
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }

    @GetMapping("/admin/list")
    public String adminList() {
        return "One List";
    }

    @GetMapping("/admin/detail")
    public String adminDetail() {
        return "One Detail";
    }


    @GetMapping("/post")
    public List<Post> getPost() {
        return List.of(
                new Post(1L, "Hola"),
                new Post(2L, "Mundo")
        );
    }


    record Post(Long id, String text) {}

}

