package com.system.controleDeRegistrosFinanceiros.auth.model;

public record RegisterDTO (String login, String password, String name, UserRole role) {
    
}
