package com.salesianostriana.dam.modelado.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Uso {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    private double coste;


    @ManyToOne
    @JoinColumn(name = "bicicleta_id")
    private Bicicleta bicicleta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "inicio_id")
    private Estacion inicio;

    @ManyToOne
    @JoinColumn(name = "fin_id")
    private Estacion fin;


    public double calcularPrecio(LocalDateTime fin) {
        /*
        Los 30 primeros minutos, gratis.
        A partir de ahí.
        30.01 minutos hasta 90.00 minutos, 0.015€ / min
        > 90.00, 0.025 / min
     */
        long duracion = ChronoUnit.MINUTES.between(fechaInicio, fin);
        double precio = 0.0;
        long calculo = duracion;

        calculo = calculo - 30;

        if (calculo > 60) {

            precio = (calculo - 60) * 0.025 + 60 * 0.015;
        } else {
            precio = calculo * 0.015;
        }

        return precio;
    }



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Uso uso = (Uso) o;
        return getId() != null && Objects.equals(getId(), uso.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
