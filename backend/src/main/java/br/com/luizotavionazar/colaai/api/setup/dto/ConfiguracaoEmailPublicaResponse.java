package br.com.luizotavionazar.colaai.api.setup.dto;

public record ConfiguracaoEmailPublicaResponse(
        String smtpHost,
        Integer smtpPort,
        String smtpUsername,
        String mailFrom,
        String frontendBaseUrl,
        boolean smtpAuth,
        boolean smtpStarttls,
        boolean setupConcluido
) {
}