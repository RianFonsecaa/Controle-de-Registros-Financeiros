package com.system.controleDeRegistrosFinanceiros.usuario.controller;

import com.system.controleDeRegistrosFinanceiros.exceptions.BusinessRuleException;
import com.system.controleDeRegistrosFinanceiros.usuario.model.RegistroDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registro(@RequestBody @Valid RegistroDTO registroDTO) {
        return ResponseEntity.ok(usuarioService.registro(registroDTO));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> buscaTodos() {
        return ResponseEntity.ok(usuarioService.buscaTodos());
    }

    @PutMapping("/{login}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable String login, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.editar(login, usuarioDTO));
    }

    @PatchMapping("/{login}/toggle")
    public ResponseEntity<Void> toggleStatus(@PathVariable String login) {
        usuarioService.toggleStatus(login);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{login}/password")
    public ResponseEntity<Void> editarSenha(@PathVariable String login, @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessRuleException("A senha deve ter no mínimo 6 caracteres.");
        }
        usuarioService.editarSenha(login, newPassword);
        return ResponseEntity.ok().build();
    }
}
