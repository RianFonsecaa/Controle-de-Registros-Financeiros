package com.system.controleDeRegistrosFinanceiros.pix.repository;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PixRepository extends JpaRepository<Pix,Long>, JpaSpecificationExecutor<Pix> {

    List<Pix> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    List<Pix> findByCobranca(Cobranca cobranca);
}
