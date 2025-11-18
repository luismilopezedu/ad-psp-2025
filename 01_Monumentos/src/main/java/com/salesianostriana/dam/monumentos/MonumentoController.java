package com.salesianostriana.dam.monumentos;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monumento/")
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
        /*return ResponseEntity.of(
                servicio.getById(id)
                        .map(MonumentoResponse::of)
        );*/
        return ResponseEntity.ok(
                MonumentoResponse.of(servicio.getById(id)));
    }


    @PostMapping
    public ResponseEntity<MonumentoResponse> create(
            @RequestBody CrearMonumentoCmd cmd
    ) {
        Monumento nuevo = servicio.save(cmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MonumentoResponse.of(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonumentoResponse> edit(
            @PathVariable Long id,
            @RequestBody CrearMonumentoCmd cmd) {

        return ResponseEntity.ok(
                MonumentoResponse.of(
                    servicio.edit(cmd, id)
        ));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
