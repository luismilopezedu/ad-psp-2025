package com.salesianostriana.dam.modelado.repos;

import com.salesianostriana.dam.modelado.modelo.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
    List<Bicicleta> findByEstaEn_IdAndEstadoOrderByModeloAsc(Long id, String estado);

    @Query("select b from Bicicleta b where b.estaEn.id = :id and b.estado = :estado order by b.modelo")
    List<Bicicleta> buscarBicisMolonas(@Param("id") Long id, @Param("estado") String estado);


}
