package com.system.controleDeRegistrosFinanceiros.dashboard.model;

import java.util.List;

public record DashboardDTO(
        ResumoGeralDTO resumoGeral,
        List<RendimentoCidadesDTO> rankingCidades,
        List<RendimentoDiarioDTO> rendimentoDiario,
        MetodoPagamentoDTO distribuicaoPagamentos
) {}