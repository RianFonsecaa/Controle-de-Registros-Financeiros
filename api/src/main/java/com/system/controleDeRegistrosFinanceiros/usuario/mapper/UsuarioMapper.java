package com.system.controleDeRegistrosFinanceiros.usuario.mapper;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.usuario.model.RegistroDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(RegistroDTO registroDTO);

    @Mapping(target = "password", ignore = true)
    void updateEntityFromDto(UsuarioDTO dto, @MappingTarget User entity);
}
