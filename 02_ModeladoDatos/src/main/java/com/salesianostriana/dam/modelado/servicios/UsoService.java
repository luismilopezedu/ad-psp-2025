package com.salesianostriana.dam.modelado.servicios;

import com.salesianostriana.dam.modelado.dto.CreateUsoRequest;
import com.salesianostriana.dam.modelado.excepciones.BadRequestException;
import com.salesianostriana.dam.modelado.modelo.Bicicleta;
import com.salesianostriana.dam.modelado.modelo.Estacion;
import com.salesianostriana.dam.modelado.modelo.Uso;
import com.salesianostriana.dam.modelado.modelo.Usuario;
import com.salesianostriana.dam.modelado.repos.BicicletaRepository;
import com.salesianostriana.dam.modelado.repos.EstacionRepository;
import com.salesianostriana.dam.modelado.repos.UsoRepository;
import com.salesianostriana.dam.modelado.repos.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsoService {

    private static final double UMBRAL_SALDO_MINIMO = 0.10;

    /*
        Los 30 primeros minutos, gratis.
        A partir de ahí.
        30.01 minutos hasta 90.00 minutos, 0.015€ / min
        > 90.00, 0.025 / min
     */


    private final UsoRepository usoRepository;
    private final EstacionRepository estacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final BicicletaRepository bicicletaRepository;
    private final BicicletaService bicicletaService;
    private final UsuarioService usuarioService;

    public Uso inicializarUso(String numTarjeta,
                              String pin,
                              Long idEstacion,
                              Long idBicicleta) {

        Usuario usuario = usuarioService.autenticacion(
                numTarjeta, pin)
                .orElseThrow(() -> new BadRequestException("La tarjeta o el pin no son válidos"));


        if (usuario.getSaldo() < UMBRAL_SALDO_MINIMO)
            throw new BadRequestException("El usuario no tiene saldo suficiente");

        Estacion inicio = estacionRepository.findById(idEstacion)
                .orElseThrow(() -> new BadRequestException("Estación no encontrada"));

        Bicicleta bicicleta = bicicletaRepository.findById(idBicicleta)
                .orElseThrow(() -> new BadRequestException("Bicicleta no encontrada"));

        Uso uso = Uso.builder()
                // Completar con los datos necesarios
                .inicio(inicio)
                .fechaInicio(LocalDateTime.now())
                .usuario(usuario)
                .bicicleta(bicicleta)
                .build();

        bicicleta.eliminarEstacion();
        bicicletaService.update(bicicleta);

        return usoRepository.save(uso);

    }


    public Uso finalizarUso(String numTarjeta,
                            String pin,
                            Long idBicicleta,
                            Long idEstacion) {

        Usuario usuario = usuarioService.autenticacion(
                        numTarjeta, pin)
                .orElseThrow(() -> new BadRequestException("La tarjeta o el pin no son válidos"));


        Uso uso = usoRepository.findByUsuarioOrderByFechaInicioDesc(usuario)
                .orElseThrow(() -> new BadRequestException("Error: el usuario no tiene ninguna bicicleta en uso actualmente"));

        Estacion fin = estacionRepository.findById(idEstacion)
                .orElseThrow(() -> new BadRequestException("Estación no encontrada"));


        if (idBicicleta != uso.getBicicleta().getId())
            throw new BadRequestException("La bicicleta no coincide con la que se tomó al inicio");

        LocalDateTime fechaFin = LocalDateTime.now();

        double precio = uso.calcularPrecio(fechaFin);

        if (usuario.getSaldo() < precio)
            throw new BadRequestException("El usuario no tiene saldo suficiente");

        uso.setFechaFin(fechaFin);
        uso.setFin(fin);
        uso.setCoste(precio);

        // Actualizar saldo del usuario
        usuarioService.actualizarSaldo(usuario, precio);

        // Actualizar estación de la bicicleta
        fin.addBicicleta(uso.getBicicleta());
        bicicletaService.update(uso.getBicicleta());

        return usoRepository.save(uso);
    }





    public Uso addUso(CreateUsoRequest createUsoRequest) {

        // Obtener datos necesarios;
        Estacion inicio = estacionRepository.findById(createUsoRequest.idEstacionOrigen())
                .orElseThrow(() -> new BadRequestException("Estación no encontrada"));

        Usuario usuario = usuarioRepository.findById(createUsoRequest.idUsuario())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        Bicicleta bicicleta = bicicletaRepository.findById(createUsoRequest.idBicicleta())
                .orElseThrow(() -> new BadRequestException("Bicicleta no encontrada"));

        Uso uso = Uso.builder()
                // Completar con los datos necesarios
                .inicio(inicio)
                .fechaInicio(LocalDateTime.now())
                .usuario(usuario)
                .bicicleta(bicicleta)
                .build();


        return usoRepository.save(uso);
    }

    public Page<Uso> getByUsuario(Long usuarioId, Pageable pageable) {
        return usoRepository.findByUsuarioId(usuarioId, pageable);
    }

}
