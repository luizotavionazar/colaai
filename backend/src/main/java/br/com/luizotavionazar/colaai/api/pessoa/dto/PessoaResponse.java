package br.com.luizotavionazar.colaai.api.pessoa.dto;

import br.com.luizotavionazar.colaai.api.endereco.dto.EnderecoResponse;
import br.com.luizotavionazar.colaai.domain.pessoa.entity.Pessoa;

import java.time.LocalDate;

public record PessoaResponse(
        Integer id,
        Integer idUsuario,
        String email,
        Integer enderecoId,
        String nome,
        String apelido,
        String fotoPerfil,
        LocalDate dataNascimento,
        String telefone,
        String pessoaEmerg,
        String contatoEmerg,
        String sexo,
        boolean possuiCpf,
        EnderecoResponse endereco
) {
    public static PessoaResponse from(Pessoa pessoa) {
        return new PessoaResponse(
                pessoa.getId(),
                pessoa.getUsuario().getId(),
                pessoa.getUsuario().getEmail(),
                pessoa.getEndereco().getId(),
                pessoa.getNome(),
                pessoa.getApelido(),
                pessoa.getFotoPerfil(),
                pessoa.getDataNascimento(),
                pessoa.getTelefone(),
                pessoa.getPessoaEmerg(),
                pessoa.getContatoEmerg(),
                pessoa.getSexo(),
                pessoa.getCpfHash() != null,
                EnderecoResponse.from(pessoa.getEndereco())
        );
    }
}