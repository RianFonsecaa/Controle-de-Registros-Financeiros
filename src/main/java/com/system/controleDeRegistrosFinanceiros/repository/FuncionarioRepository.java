package com.system.controleDeRegistrosFinanceiros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.model.entidades.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {    
}
