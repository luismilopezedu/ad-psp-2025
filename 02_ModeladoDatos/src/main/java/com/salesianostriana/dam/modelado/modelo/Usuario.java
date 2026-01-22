package com.salesianostriana.dam.modelado.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String numTarjeta;
    private double saldo;
    private String pin;

    // ¿Usuario - Uso?


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuario usuario = (Usuario) o;
        return getId() != null && Objects.equals(getId(), usuario.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }


    public static class Specs {

        public static PredicateSpecification<Usuario> numTarjeta(String numTarjeta) {
            return (root, cb) ->
                    numTarjeta == null ? null : cb.equal(root.get("numTarjeta"), numTarjeta);
        }

    }

}
