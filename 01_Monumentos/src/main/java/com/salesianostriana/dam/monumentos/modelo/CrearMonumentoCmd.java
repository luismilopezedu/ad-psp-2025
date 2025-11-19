package com.salesianostriana.dam.monumentos.modelo;

public record CrearMonumentoCmd(
        String isoCode,
        String pais,
        String localizacion,
        String nombre,
        String descripcion,
        String url
) {

    public Monumento toEntity() {
        return Monumento.builder()
                .isoCode(this.isoCode())
                .pais(this.pais())
                .nombre(this.nombre())
                .descripcion(this.descripcion())
                .localizacion(this.localizacion())
                .url(this.url())
                .build();
    }

}

