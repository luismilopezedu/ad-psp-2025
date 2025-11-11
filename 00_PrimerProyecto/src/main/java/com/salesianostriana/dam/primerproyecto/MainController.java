package com.salesianostriana.dam.primerproyecto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/person")
    public Person getPerson() {
        return new Person(1L, "Luismi", "López");
    }


    record Person(Long id, String firstName, String lastName){}

}
