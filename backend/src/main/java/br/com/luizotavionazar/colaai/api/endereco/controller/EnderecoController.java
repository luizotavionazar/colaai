package br.com.luizotavionazar.colaai.api.endereco.controller;

import br.com.luizotavionazar.colaai.api.endereco.dto.EnderecoRequest;
import br.com.luizotavionazar.colaai.api.endereco.dto.EnderecoResponse;
import br.com.luizotavionazar.colaai.domain.endereco.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping("/cadastro")
    public ResponseEntity<EnderecoResponse> cadastrarCompleto(@Valid @RequestBody EnderecoRequest request) {
        EnderecoResponse response = enderecoService.cadastrarCompleto(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}