package com.salesianostriana.dam.monumentos.servicio;

import com.salesianostriana.dam.monumentos.error.MonumentoNotFoundException;
import com.salesianostriana.dam.monumentos.modelo.CrearMonumentoCmd;
import com.salesianostriana.dam.monumentos.modelo.Monumento;
import com.salesianostriana.dam.monumentos.modelo.MonumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonumentoServicio {

    private final MonumentoRepository monumentoRepository;

    public List<Monumento> getAll() {
        List<Monumento> result =  monumentoRepository.findAll();

        if (result.isEmpty())
            throw new MonumentoNotFoundException("No hay monumentos registrados");

        return result;
    }

    public Monumento getById(Long id) {
        return monumentoRepository.findById(id)
                .orElseThrow(() -> new MonumentoNotFoundException(id));
    }

    public Monumento save(CrearMonumentoCmd cmd) {

        if (!StringUtils.hasText(cmd.nombre())) {
            throw new IllegalArgumentException("Error al crear un monumento");
        }

        return monumentoRepository.save(cmd.toEntity());
    }

    public Monumento edit(CrearMonumentoCmd cmd, Long id) {
        return monumentoRepository.findById(id)
                .map(monumento -> {
                    monumento.setIsoCode(cmd.isoCode());
                    monumento.setPais(cmd.pais());
                    monumento.setNombre(cmd.nombre());
                    monumento.setDescripcion(cmd.descripcion());
                    monumento.setLocalizacion(cmd.localizacion());
                    monumento.setUrl(cmd.descripcion());

                    return monumentoRepository.save(monumento);
                })
                .orElseThrow(() -> new RuntimeException());
    }

    public void delete(Monumento monumento) {
        deleteById(monumento.getId());
    }

    public void deleteById(Long id) {
        monumentoRepository.deleteById(id);
    }


}
