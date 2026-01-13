package com.salesianostriana.dam.modelado.dto;

import com.salesianostriana.dam.modelado.modelo.Uso;

import java.time.LocalDateTime;

public record SuperBasicUsoDto(
        Long id,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Long idUsuario,
        double coste
) {

    public static SuperBasicUsoDto of(Uso uso) {
        return new SuperBasicUsoDto(
                uso.getId(),
                uso.getFechaInicio(),
                uso.getFechaFin(),
                uso.getUsuario() != null ? uso.getUsuario().getId() : null,
                uso.getCoste()
        );
    }

}
