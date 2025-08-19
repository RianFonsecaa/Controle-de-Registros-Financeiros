package com.system.controleDeRegistrosFinanceiros.cidade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;


@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    boolean existsByNome(String cidadeNome);
}
