package com.system.controleDeRegistrosFinanceiros.cidade.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CidadeDTO {
    private Long id;
    private String nome;
}

