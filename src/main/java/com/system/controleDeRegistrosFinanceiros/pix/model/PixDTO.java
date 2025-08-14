package com.system.controleDeRegistrosFinanceiros.pix.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PixDTO {
    private Long id;
    private String cliente;
    private Double valor;
    private LocalDate data;
}