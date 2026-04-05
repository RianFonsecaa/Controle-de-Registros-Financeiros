package com.system.controleDeRegistrosFinanceiros.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ReportGenerationException extends RuntimeException{

    public ReportGenerationException(String message, Throwable cause){

        super("Não foi possível gerar o relatório! Causa:\n", cause);
    }
}
