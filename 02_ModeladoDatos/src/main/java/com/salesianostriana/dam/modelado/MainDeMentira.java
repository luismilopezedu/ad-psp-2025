package com.salesianostriana.dam.modelado;

import com.salesianostriana.dam.modelado.servicios.UsoService;
import com.salesianostriana.dam.modelado.servicios.UsuarioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainDeMentira {

    //private final UsoService usoService;

    private final UsuarioService usuarioService;


    @PostConstruct
    public void main() {

        //usoService.inicializarUso("0073-0091-0037-0019","0097", 1L, 2L);

        //usoService.finalizarUso("0073-0091-0037-0019","0097", 2L, 3L);

        usuarioService.filtrar("1", null, null, null)
                .forEach(System.out::println);


    }

}
