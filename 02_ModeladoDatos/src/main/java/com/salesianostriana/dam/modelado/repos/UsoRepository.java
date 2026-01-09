package com.salesianostriana.dam.modelado.repos;

import com.salesianostriana.dam.modelado.modelo.Uso;
import com.salesianostriana.dam.modelado.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsoRepository extends JpaRepository<Uso, Long> {


    @Query("""
            select u 
            from Uso u 
            where u.usuario = ?1
              and u.fechaFin is null 
            order by u.fechaInicio DESC
            """)
    Optional<Uso> findByUsuarioOrderByFechaInicioDesc(Usuario usuario);
}
