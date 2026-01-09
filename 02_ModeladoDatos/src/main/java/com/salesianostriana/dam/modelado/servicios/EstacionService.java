package com.salesianostriana.dam.modelado.servicios;

import com.salesianostriana.dam.modelado.excepciones.EntityNotFoundException;
import com.salesianostriana.dam.modelado.modelo.Estacion;
import com.salesianostriana.dam.modelado.repos.EstacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacionService {

    private final EstacionRepository estacionRepository;

    public List<Estacion> getAll() {
        List<Estacion> estaciones = estacionRepository.findAll();

        if (estaciones.isEmpty()) {
            throw new EntityNotFoundException("No hay estaciones disponibles");
        }

        return estaciones;
    }

    public Estacion getEstacionById(Long id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estación no encontrada"));
    }


    public Estacion addEstacion(Estacion e) {
        return estacionRepository.save(e);
    }

    public Estacion editEstacion(Long id, Estacion e) {
        return estacionRepository.findById(id)
                .map(estacion -> {
                    estacion.setNombre(e.getNombre());
                    estacion.setCapacidad(e.getCapacidad());
                    estacion.setNumero(e.getNumero());
                    estacion.setCoordenadas(e.getCoordenadas());
                    return estacionRepository.save(estacion);
                })
                .orElseThrow(() -> new EntityNotFoundException("Estación no encontrada"));

    }

    public void deleteEstacion(Long id) {
        estacionRepository.deleteById(id);
    }

}
