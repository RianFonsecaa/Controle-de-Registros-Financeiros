package com.system.controleDeRegistrosFinanceiros.funcionario.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.cidade.model.entity.Cidade;
import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;

import java.util.List;
import java.util.Optional;


@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {   
    
    boolean existsByNome(String funcionarioNome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    Page<Funcionario> findByAtivo(boolean ativo, Pageable pageable);

    List<Funcionario> findByAtivoTrueOrderByNomeAsc();
    
}
