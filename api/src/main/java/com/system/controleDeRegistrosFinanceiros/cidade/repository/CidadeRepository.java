package com.system.controleDeRegistrosFinanceiros.cidade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;

import java.util.List;
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    boolean existsByNome(String cidadeNome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    Page<Cidade> findByAtivo(boolean ativo, Pageable pageable);

    List<Cidade> findByAtivoTrueOrderByNomeAsc();
}
