package br.com.luizotavionazar.colaai.api.pessoa.controller;

import br.com.luizotavionazar.colaai.api.pessoa.dto.PessoaRequest;
import br.com.luizotavionazar.colaai.api.pessoa.dto.PessoaResponse;
import br.com.luizotavionazar.colaai.domain.pessoa.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @PutMapping("/{id}/completar-cadastro")
    public ResponseEntity<PessoaResponse> completarCadastro(
            @PathVariable Integer id,
            @Valid @RequestBody PessoaRequest request) {
        return ResponseEntity.ok(pessoaService.completarCadastro(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pessoaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PessoaResponse> buscarPoridUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(pessoaService.buscarPoridUsuario(idUsuario));
    }
}