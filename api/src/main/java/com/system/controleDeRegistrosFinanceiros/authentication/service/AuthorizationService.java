package com.system.controleDeRegistrosFinanceiros.authentication.service;

import com.system.controleDeRegistrosFinanceiros.authentication.model.*;
import com.system.controleDeRegistrosFinanceiros.authentication.utils.TokenService;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.system.controleDeRegistrosFinanceiros.authentication.repository.UserRepository;

import java.util.List;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", username));
    }

    public LoginResponseDTO login(LoginRequestDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();

            var accessToken = tokenService.generateAccessToken(user);
            var refreshToken = tokenService.generateRefreshToken(user);

            return new LoginResponseDTO(accessToken, refreshToken);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Credenciais inválidas. Verifique seu login e senha.");
        }
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("O atributo 'refreshToken' é obrigatório.");
        }
        String login = tokenService.validateRefreshToken(refreshToken);
        
        User user = (User) userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", login));

        var newAccessToken = tokenService.generateAccessToken(user);
        var newRefreshToken = tokenService.generateRefreshToken(user);

        return new LoginResponseDTO(newAccessToken, newRefreshToken);
    }


}
