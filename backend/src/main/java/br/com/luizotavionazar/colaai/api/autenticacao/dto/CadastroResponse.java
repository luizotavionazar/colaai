package br.com.luizotavionazar.colaai.api.autenticacao.dto;

import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record CadastroResponse(
        Integer id,
        String email,
        Set<String> papeis
) {
    public static CadastroResponse from(Usuario usuario) {
        return new CadastroResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPapeis().stream()
                        .map(link -> link.getPapel().getPapel())
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}