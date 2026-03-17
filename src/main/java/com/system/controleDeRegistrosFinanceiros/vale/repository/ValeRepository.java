package com.system.controleDeRegistrosFinanceiros.vale.repository;

import com.system.controleDeRegistrosFinanceiros.vale.model.entity.Vale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ValeRepository extends JpaRepository<Vale, Long>, JpaSpecificationExecutor<Vale> {
}
