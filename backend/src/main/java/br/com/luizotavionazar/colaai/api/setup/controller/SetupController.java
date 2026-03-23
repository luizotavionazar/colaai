package br.com.luizotavionazar.colaai.api.setup.controller;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.MensagemResponse;
import br.com.luizotavionazar.colaai.api.setup.dto.ConfiguracaoEmailPublicaResponse;
import br.com.luizotavionazar.colaai.api.setup.dto.SalvarSetupRequest;
import br.com.luizotavionazar.colaai.api.setup.dto.StatusSetupResponse;
import br.com.luizotavionazar.colaai.domain.configuracao.service.SetupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {

    private final SetupService setupService;

    @GetMapping("/status")
    public ResponseEntity<StatusSetupResponse> status() {
        return ResponseEntity.ok(setupService.status());
    }

    @GetMapping
    public ResponseEntity<ConfiguracaoEmailPublicaResponse> obter() {
        return ResponseEntity.ok(setupService.obterConfiguracaoPublica());
    }

    @PostMapping
    public ResponseEntity<MensagemResponse> salvar(@Valid @RequestBody SalvarSetupRequest request) {
        setupService.salvar(request);
        return ResponseEntity.ok(new MensagemResponse("Setup salvo com sucesso."));
    }
}