package com.salesianostriana.dam.modelado;

import com.salesianostriana.dam.modelado.servicios.UsoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainDeMentira {

    private final UsoService usoService;


    @PostConstruct
    public void main() {

        usoService.inicializarUso("0073-0091-0037-0019","0097", 1L, 2L);

        usoService.finalizarUso("0073-0091-0037-0019","0097", 2L, 3L);


    }

}
