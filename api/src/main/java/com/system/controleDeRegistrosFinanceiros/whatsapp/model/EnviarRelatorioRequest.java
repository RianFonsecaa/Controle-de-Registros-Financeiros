package com.system.controleDeRegistrosFinanceiros.whatsapp.model;

import java.util.List;

import com.system.controleDeRegistrosFinanceiros.cobranca.model.dto.CobrancaQueryFilters;

public record EnviarRelatorioRequest(
    CobrancaQueryFilters filtros,
    List<String> numerosTelefone,
    String mensagemPersonalizada
) {}
