package com.system.controleDeRegistrosFinanceiros.pix.repository;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.entity.Cobranca;
import com.system.controleDeRegistrosFinanceiros.pix.model.dto.PixDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;

import java.util.List;

@Repository
public interface PixRepository extends JpaRepository<Pix,Long> {
    List<Pix> findByCobranca(Cobranca cobranca);
}
