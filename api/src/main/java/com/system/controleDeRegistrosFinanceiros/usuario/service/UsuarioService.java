package com.system.controleDeRegistrosFinanceiros.usuario.service;

import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.authentication.repository.UserRepository;
import com.system.controleDeRegistrosFinanceiros.exceptions.BusinessRuleException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import com.system.controleDeRegistrosFinanceiros.usuario.mapper.UsuarioMapper;
import com.system.controleDeRegistrosFinanceiros.usuario.model.RegistroDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public UsuarioDTO registro(RegistroDTO registroDTO) {
        if (this.userRepository.existsByLogin(registroDTO.login())) {
            throw new ResourceAlreadyExistsException("Já existe um usuário cadastrado com esse E-mail!");
        }

        User newUser = usuarioMapper.toEntity(registroDTO);
        newUser.setPassword(passwordEncoder.encode(registroDTO.password()));

        userRepository.save(newUser);
        return usuarioMapper.toDTO(newUser);
    }

    public List<UsuarioDTO> buscaTodos() {
        return userRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    public UsuarioDTO editar(String login, UsuarioDTO data) {
        User user = (User) userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", login));

        usuarioMapper.updateEntityFromDto(data, user);

        userRepository.save(user);
        return usuarioMapper.toDTO(user);
    }

    public void toggleStatus(String login) {
        String emailLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (login.equals(emailLogado)) {
            throw new BusinessRuleException("Não é permitido excluir o próprio usuário logado.");
        }

        User usuario = (User) userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", login));
        usuario.setAtivo(!usuario.getAtivo());
        userRepository.save(usuario);
    }

    public void editarSenha(String login, String newPassword) {
;
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessRuleException("A senha deve ter no mínimo 6 caracteres.");
        }

        User user = (User) userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", login));

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }
}
