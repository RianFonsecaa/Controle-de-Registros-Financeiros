package com.system.controleDeRegistrosFinanceiros.funcionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.funcionario.model.Funcionario;


@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {    
}
