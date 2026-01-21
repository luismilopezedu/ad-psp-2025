package com.salesianostriana.dam.modelado.servicios;

import com.salesianostriana.dam.modelado.modelo.Usuario;
import com.salesianostriana.dam.modelado.repos.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public Optional<Usuario> autenticacion(String numTarjeta, String pin) {
        return usuarioRepository.findByNumTarjetaAndPin(numTarjeta, pin);
    }

    public void actualizarSaldo(Usuario usuario, double cantidadDecrementar) {
        usuario.setSaldo(usuario.getSaldo() - cantidadDecrementar);
        usuarioRepository.save(usuario);
    }


    public List<Usuario> filtrar(String nombre, String numTarjeta, Double saldoMinimo, Double saldoMaximo) {
        return usuarioRepository.findAll(
                Specification.where(UsuarioSpecs.nombreContainsv4(nombre))
        );
    }


    private static class UsuarioSpecs {

        static Specification<Usuario> nombreContains(String nombre) {
            return (root, query, cb) ->
                    nombre == null ? null : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        }

        static PredicateSpecification<Usuario> nombreContainsv4(String nombre) {
            return (from, builder) ->
                    nombre == null ? null : builder.like(builder.lower(from.get("nombre")), "%" + nombre.toLowerCase() + "%");
        }



    }


}
