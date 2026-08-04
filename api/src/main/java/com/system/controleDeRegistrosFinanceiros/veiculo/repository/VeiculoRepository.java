package com.system.controleDeRegistrosFinanceiros.veiculo.repository;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.veiculo.model.entity.Veiculo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);

    Page<Veiculo> findByAtivo(boolean ativo, Pageable pageable);

    List<Veiculo> findByAtivoTrueOrderByModeloAsc();
    

}
