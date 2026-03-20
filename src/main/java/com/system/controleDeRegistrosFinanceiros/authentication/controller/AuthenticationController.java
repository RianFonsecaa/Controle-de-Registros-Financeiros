package com.system.controleDeRegistrosFinanceiros.authentication.controller;

import java.util.List;
import java.util.Map;

import com.system.controleDeRegistrosFinanceiros.authentication.model.*;
import com.system.controleDeRegistrosFinanceiros.authentication.service.AuthorizationService;
import com.system.controleDeRegistrosFinanceiros.exceptions.BusinessRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        return ResponseEntity.ok(authorizationService.login(data));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authorizationService.refreshToken(request.get("refreshToken")));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDTO data) {
        return ResponseEntity.ok(authorizationService.register(data));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(authorizationService.findAllUsers());
    }

    @PutMapping("/users/{login}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String login, @RequestBody @Valid UserDTO data) {
        return ResponseEntity.ok(authorizationService.updateUser(login, data));
    }

    @DeleteMapping("/users/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        authorizationService.deleteUser(login);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{login}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable String login, @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessRuleException("A senha deve ter no mínimo 6 caracteres.");
        }
        authorizationService.updatePassword(login, newPassword);
        return ResponseEntity.ok().build();
    }
}
