package com.salesianostriana.dam.modelado.modelo;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@ToString
public class Estacion {

    @Id @GeneratedValue
    private Long id;

    private int numero;
    private String nombre;
    private String coordenadas;
    private int capacidad;

    // ¿Estación - Bicicleta?

    @Builder.Default
    @OneToMany(mappedBy = "estaEn", fetch = FetchType.EAGER)
    private List<Bicicleta> bicicletas = new ArrayList<>();


    // Métodos de utilidad

    public void addBicicleta(Bicicleta bicicleta) {
        bicicletas.add(bicicleta);
        bicicleta.setEstaEn(this);
    }

    public void removeBicicleta(Bicicleta bicicleta) {
        bicicletas.remove(bicicleta);
        bicicleta.setEstaEn(null);
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Estacion estacion = (Estacion) o;
        return getId() != null && Objects.equals(getId(), estacion.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
