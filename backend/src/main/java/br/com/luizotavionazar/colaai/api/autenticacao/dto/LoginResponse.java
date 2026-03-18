package br.com.luizotavionazar.colaai.api.autenticacao.dto;

import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record LoginResponse(
        Integer idUsuario,
        String email,
        Set<String> papeis,
        String mensagem
) {
    public static LoginResponse from(Usuario usuario) {
        return new LoginResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPapeis().stream()
                        .map(link -> link.getPapel().getPapel())
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                "Login realizado com sucesso"
        );
    }
}