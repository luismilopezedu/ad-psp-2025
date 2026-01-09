package com.salesianostriana.dam.modelado.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Bicicleta {

    @Id
    @GeneratedValue
    private Long id;

    private String marca;
    private String modelo;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "esta_en_id",
            foreignKey = @ForeignKey(name = "fk_bicicleta_estacion"))
    private Estacion estaEn;


    public void eliminarEstacion() {
        if (estaEn != null) {
            estaEn.removeBicicleta(this);
        }
    }



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Bicicleta bicicleta = (Bicicleta) o;
        return getId() != null && Objects.equals(getId(), bicicleta.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
