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

}
