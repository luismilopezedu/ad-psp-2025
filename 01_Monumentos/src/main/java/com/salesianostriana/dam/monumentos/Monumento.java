package com.salesianostriana.dam.monumentos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Monumento {

    @Id
    @GeneratedValue
    private Long id;
    private String isoCode;
    private String pais;
    private String localizacion;
    private String nombre;
    private String descripcion;
    private String url;


}
