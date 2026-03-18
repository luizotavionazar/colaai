package br.com.luizotavionazar.colaai.api.endereco.dto;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;

public record EnderecoResponse(
        Integer id,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        CidadeResponse cidade
) {
    public static EnderecoResponse from(Endereco endereco) {
        return new EnderecoResponse(
                endereco.getId(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                CidadeResponse.from(endereco.getCidade())
        );
    }
}