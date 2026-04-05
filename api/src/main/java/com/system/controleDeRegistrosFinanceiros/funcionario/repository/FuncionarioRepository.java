package com.system.controleDeRegistrosFinanceiros.funcionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.entity.Funcionario;

import java.util.Optional;


@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {   
    
    boolean existsByNome(String funcionarioNome);

    boolean existsByNomeAndIdNot(String nome, Long id);
    
}
