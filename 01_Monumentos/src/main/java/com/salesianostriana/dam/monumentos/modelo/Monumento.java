package com.salesianostriana.dam.monumentos.modelo;

import com.salesianostriana.dam.monumentos.TipoMonumento;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private TipoMonumento tipo;


}
