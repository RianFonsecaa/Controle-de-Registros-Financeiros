package com.system.controleDeRegistrosFinanceiros.usuario.model;

import com.system.controleDeRegistrosFinanceiros.authentication.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "O login é obrigatório")
        @Email(message = "E-mail inválido")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        @NotNull(message = "A role é obrigatória")
        UserRole role,

        Boolean ativo
) {
}
