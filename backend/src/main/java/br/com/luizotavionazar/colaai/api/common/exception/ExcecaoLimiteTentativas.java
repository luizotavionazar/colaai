package br.com.luizotavionazar.colaai.api.common.exception;

import lombok.Getter;

@Getter
public class ExcecaoLimiteTentativas extends RuntimeException {

    private final long retryAfterSeconds;

    public ExcecaoLimiteTentativas(String message, long secTentarNovamente) {
        super(message);
        this.retryAfterSeconds = secTentarNovamente;
    }
}