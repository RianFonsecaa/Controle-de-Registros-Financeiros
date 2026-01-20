package com.system.controleDeRegistrosFinanceiros.authentication.controller;

import java.util.Map;
import java.util.Optional;

import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceAlreadyExistsException;
import com.system.controleDeRegistrosFinanceiros.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.system.controleDeRegistrosFinanceiros.authentication.model.LoginRequestDTO;
import com.system.controleDeRegistrosFinanceiros.authentication.model.LoginResponseDTO;
import com.system.controleDeRegistrosFinanceiros.authentication.model.RegisterDTO;
import com.system.controleDeRegistrosFinanceiros.authentication.model.User;
import com.system.controleDeRegistrosFinanceiros.authentication.repository.UserRepository;
import com.system.controleDeRegistrosFinanceiros.authentication.utils.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (User) auth.getPrincipal();

            var accessToken = tokenService.generateAccessToken(user);
            var refreshToken = tokenService.generateRefreshToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Credenciais inválidas. Verifique seu login e senha.");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("O atributo 'refreshToken' é obrigatório no corpo da requisição.");
        }

        String login = tokenService.validateRefreshToken(refreshToken);
        User user = (User) userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", login));

        var newAccessToken = tokenService.generateAccessToken(user);
        var newRefreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, newRefreshToken));
    }
    @PostMapping("/register")
        public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO data){
            if (this.userRepository.existsByLogin(data.login())){
                throw new ResourceAlreadyExistsException("Usuário", "E-mail", data.login());
            };
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.login(), encryptedPassword, data.name(), data.role());
        return ResponseEntity.ok(this.userRepository.save(newUser));
    }



}
