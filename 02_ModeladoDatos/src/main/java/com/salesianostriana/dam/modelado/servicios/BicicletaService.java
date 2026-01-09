package com.salesianostriana.dam.modelado.servicios;

import com.salesianostriana.dam.modelado.excepciones.EntityNotFoundException;
import com.salesianostriana.dam.modelado.modelo.Bicicleta;
import com.salesianostriana.dam.modelado.repos.BicicletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;


    public Bicicleta update(Bicicleta bicicleta) {
        return bicicletaRepository.findById(bicicleta.getId())
                .map(b -> {
                    b.setEstado(bicicleta.getEstado());
                    b.setEstaEn(bicicleta.getEstaEn());
                    b.setModelo(bicicleta.getModelo());
                    b.setMarca(bicicleta.getMarca());
                    return bicicletaRepository.save(b);
                })
                .orElseThrow(() -> new EntityNotFoundException("Bicicleta no encontrada"));
     }


}
