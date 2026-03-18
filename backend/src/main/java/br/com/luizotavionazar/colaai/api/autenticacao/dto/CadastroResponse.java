package br.com.luizotavionazar.colaai.api.autenticacao.dto;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record CadastroResponse(
        Integer idUsuario,
        String nome,
        String email,
        String telefone,
        String sexo,
        LocalDate dataNascimento,
        Integer codIbgeCidade,
        String cidade,
        String uf,
        Set<String> papeis
) {
    public static CadastroResponse from(Usuario usuario, CadastroRequest request, Cidade cidade) {
        return new CadastroResponse(
                usuario.getId(),
                request.nomeNormalizado(),
                usuario.getEmail(),
                request.telefoneNormalizado(),
                request.sexoNormalizado(),
                request.dataNascimento(),
                cidade.getCodIbge(),
                cidade.getNome(),
                cidade.getUf(),
                usuario.getPapeis().stream()
                        .map(link -> link.getPapel().getPapel())
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }
}