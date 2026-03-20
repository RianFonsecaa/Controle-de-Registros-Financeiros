package com.system.controleDeRegistrosFinanceiros.authentication.model;

public record UserDTO(
        String name,
        String login,
        UserRole role
) {}