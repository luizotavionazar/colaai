package br.com.luizotavionazar.colaai.api.autenticacao.controller;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroResponse;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import br.com.luizotavionazar.colaai.domain.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroRequest request) {
        try {
            Usuario usuario = usuarioService.cadastrar(request.email(), request.senha());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(CadastroResponse.from(usuario));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", ex.getMessage()));
        }
    }
}