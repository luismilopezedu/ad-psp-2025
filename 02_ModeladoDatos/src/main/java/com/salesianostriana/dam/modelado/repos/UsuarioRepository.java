package com.salesianostriana.dam.modelado.repos;

import com.salesianostriana.dam.modelado.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>,
        JpaSpecificationExecutor<Usuario> {

    @Query("select u from Usuario u where u.numTarjeta = ?1 and u.pin = ?2")
    Optional<Usuario> findByNumTarjetaAndPin(String numTarjeta, String pin);




}
