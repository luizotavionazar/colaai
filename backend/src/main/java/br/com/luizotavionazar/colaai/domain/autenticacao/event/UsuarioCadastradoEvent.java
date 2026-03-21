package br.com.luizotavionazar.colaai.domain.autenticacao.event;

public record UsuarioCadastradoEvent(
        Integer idUsuario,
        String nome,
        String email
) {
}