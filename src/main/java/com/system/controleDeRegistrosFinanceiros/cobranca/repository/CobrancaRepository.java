package com.system.controleDeRegistrosFinanceiros.cobranca.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;


@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, Long>, JpaSpecificationExecutor<Cobranca> {

    List<Cobranca> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
}