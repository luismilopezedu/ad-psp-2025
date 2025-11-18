package com.salesianostriana.dam.monumentos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonumentoServicio {

    private final MonumentoRepository monumentoRepository;

    public List<Monumento> getAll() {
        return monumentoRepository.findAll();
    }

    public Optional<Monumento> getById(Long id) {
        return monumentoRepository.findById(id);
    }

    public Monumento save(CrearMonumentoCmd cmd) {
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
