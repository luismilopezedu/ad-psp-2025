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

    @GetMapping("/user")
    public String user() {
        return "Hello User!";
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



}

