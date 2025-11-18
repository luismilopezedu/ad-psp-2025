package com.salesianostriana.dam.monumentos;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monumento")
@RequiredArgsConstructor
public class MonumentoController {

    private final MonumentoServicio servicio;

    @GetMapping
    public List<MonumentoResponse> getAll(){
        return servicio.getAll()
                .stream()
                .map(monumento -> MonumentoResponse.of(monumento))
                //.map(MonumentoResponse::of)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonumentoResponse> getById(@PathVariable Long id){
        return ResponseEntity.of(
                servicio.getById(id)
                        .map(MonumentoResponse::of)
        );
    }


}
