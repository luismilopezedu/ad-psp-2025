package com.salesianostriana.dam.modelado;

import com.salesianostriana.dam.modelado.dto.SuperBasicUsoDto;
import com.salesianostriana.dam.modelado.modelo.Uso;
import com.salesianostriana.dam.modelado.servicios.UsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uso")
@RequiredArgsConstructor
public class UsoController {

    private final UsoService usoService;

    @GetMapping("/user/{id}")
    public Page<SuperBasicUsoDto> getUsosByUserId(
            @PathVariable Long id,
        @PageableDefault(page = 0, size = 20) Pageable pageable) {
        //return usoService.getByUsuario(id, pageable);
        return usoService.getByUsuario(id, pageable)
                .map(SuperBasicUsoDto::of);
    }

}
