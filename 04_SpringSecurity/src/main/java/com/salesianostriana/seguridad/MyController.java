package com.salesianostriana.seguridad;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping
    public String endpoint() {
        return "Hello World!";
    }

    @PreAuthorize("hasRole('ADMIN') and hasRole('MANAGER')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }

}

