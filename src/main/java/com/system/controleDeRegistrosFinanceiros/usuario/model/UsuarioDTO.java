package com.system.controleDeRegistrosFinanceiros.usuario.model;

import com.system.controleDeRegistrosFinanceiros.authentication.model.UserRole;

public record UsuarioDTO(
        String name,
        String login,
        UserRole role,
        Boolean ativo
) {}