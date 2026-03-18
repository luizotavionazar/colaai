package br.com.luizotavionazar.colaai.api.endereco.controller;

import br.com.luizotavionazar.colaai.api.endereco.dto.CidadeResponse;
import br.com.luizotavionazar.colaai.domain.endereco.service.CidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<CidadeResponse>> listarCidades() {
        return ResponseEntity.ok(cidadeService.listarCidades());
    }
}