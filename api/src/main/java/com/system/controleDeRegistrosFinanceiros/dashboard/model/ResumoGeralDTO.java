package com.system.controleDeRegistrosFinanceiros.dashboard.model;

public record ResumoGeralDTO(
        Double totalArrecadado,
        Double totalPix,
        Double totalEspecie,
        Long cidadesAtendidas
) { }
