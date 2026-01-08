package com.system.controleDeRegistrosFinanceiros.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        String message = "Ocorreu um erro inesperado no servidor. Por favor, contate o administrador.";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse apiError = new ErrorResponse(status, message);
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ReportGenerationException.class)
    public ResponseEntity<ErrorResponse> handleReportGenerationException(ReportGenerationException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas. Verifique seu login e senha.");
    }

    @ExceptionHandler({JWTVerificationException.class, TokenExpiredException.class})
    public ResponseEntity<ErrorResponse> handleJwtExceptions(Exception ex, WebRequest request) {
        String message = "Token inválido ou expirado.";
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSize(MaxUploadSizeExceededException e) {
        return buildErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "O comprovante excede o tamanho máximo permitido"
        );
    }
}
