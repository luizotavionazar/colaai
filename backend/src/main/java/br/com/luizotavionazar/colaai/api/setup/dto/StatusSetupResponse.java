package br.com.luizotavionazar.colaai.api.setup.dto;

public record StatusSetupResponse(
        boolean bootstrapOk,
        boolean setupConcluido
) {
}