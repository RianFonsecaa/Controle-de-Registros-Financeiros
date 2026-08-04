package com.system.controleDeRegistrosFinanceiros.usuario.controller;

import com.system.controleDeRegistrosFinanceiros.usuario.model.RegistroDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.model.UsuarioDTO;
import com.system.controleDeRegistrosFinanceiros.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registro(@RequestBody @Valid RegistroDTO registroDTO) {
        return ResponseEntity.ok(usuarioService.registro(registroDTO));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> buscaTodos(
         @RequestParam(required = false) Boolean ativo,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(usuarioService.buscaTodos(ativo, pageable));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<UsuarioDTO>> buscaAtivos(){
        return ResponseEntity.ok(usuarioService.buscaAtivos());
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

        usuarioService.editarSenha(login, request.get("password"));
        return ResponseEntity.ok().build();
    }
}
