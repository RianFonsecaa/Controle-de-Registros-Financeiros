package com.system.controleDeRegistrosFinanceiros.cobranca.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.system.controleDeRegistrosFinanceiros.dashboard.model.RendimentoDiarioDTO;
import com.system.controleDeRegistrosFinanceiros.dashboard.model.RendimentoCidadesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;


@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, Long>, JpaSpecificationExecutor<Cobranca> {

    List<Cobranca> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
}