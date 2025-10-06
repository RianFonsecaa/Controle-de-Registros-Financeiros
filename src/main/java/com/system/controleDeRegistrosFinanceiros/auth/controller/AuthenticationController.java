package com.system.controleDeRegistrosFinanceiros.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.controleDeRegistrosFinanceiros.auth.model.LoginRequestDTO;
import com.system.controleDeRegistrosFinanceiros.auth.model.LoginResponseDTO;
import com.system.controleDeRegistrosFinanceiros.auth.model.RegisterDTO;
import com.system.controleDeRegistrosFinanceiros.auth.model.User;
import com.system.controleDeRegistrosFinanceiros.auth.repository.UserRepository;
import com.system.controleDeRegistrosFinanceiros.security.TokenService;

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
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();

        var accessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        var login = tokenService.validateToken(refreshToken);
        if (login.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        var user = (User) userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        var newAccessToken = tokenService.generateAccessToken(user);
        var newRefreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, newRefreshToken));
    }

    @PostMapping("/register")
        public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO data){
            if (this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.login(), encryptedPassword, data.name(), data.role());
        return ResponseEntity.ok(this.userRepository.save(newUser));
    }

}
