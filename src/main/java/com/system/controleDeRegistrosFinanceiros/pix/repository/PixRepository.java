package com.system.controleDeRegistrosFinanceiros.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.controleDeRegistrosFinanceiros.pix.model.entity.Pix;

@Repository
public interface PixRepository extends JpaRepository<Pix,Long> {
    
}
