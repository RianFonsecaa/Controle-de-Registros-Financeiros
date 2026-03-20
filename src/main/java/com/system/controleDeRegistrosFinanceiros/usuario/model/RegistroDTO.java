package com.system.controleDeRegistrosFinanceiros.usuario.model;

import com.system.controleDeRegistrosFinanceiros.authentication.model.UserRole;

public record RegistroDTO (String login, String password, String name, UserRole role) {

}