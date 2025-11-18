package com.system.controleDeRegistrosFinanceiros.authentication.model;

public record RegisterDTO (String login, String password, String name, UserRole role) {
    
}
