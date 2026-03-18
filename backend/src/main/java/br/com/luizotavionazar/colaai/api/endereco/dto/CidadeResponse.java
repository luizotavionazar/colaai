package br.com.luizotavionazar.colaai.api.endereco.dto;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;

public record CidadeResponse(
        Integer codIbge,
        String nome,
        String uf
) {
    public static CidadeResponse from(Cidade cidade) {
        return new CidadeResponse(
                cidade.getCodIbge(),
                cidade.getNome(),
                cidade.getUf()
        );
    }
}