package br.com.luizotavionazar.colaai.api.autenticacao.controller;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroResponse;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.LoginRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.LoginResponse;
import br.com.luizotavionazar.colaai.domain.autenticacao.service.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponse> cadastrar(@Valid @RequestBody CadastroRequest request) {
        CadastroResponse response = autenticacaoService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = autenticacaoService.login(request);
    
        return ResponseEntity.ok(response);
    }
}