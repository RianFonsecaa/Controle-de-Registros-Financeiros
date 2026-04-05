package com.system.controleDeRegistrosFinanceiros.veiculo.repository;

import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);

}
