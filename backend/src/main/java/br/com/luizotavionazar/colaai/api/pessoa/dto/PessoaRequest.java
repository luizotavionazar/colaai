package br.com.luizotavionazar.colaai.api.pessoa.dto;

import jakarta.validation.constraints.Size;

public record PessoaRequest(

        @Size(max = 100, message = "O apelido deve ter até 100 caracteres")
        String apelido,

        @Size(max = 255, message = "A foto de perfil deve ter até 255 caracteres")
        String fotoPerfil,

        String cpf,

        @Size(max = 100, message = "A pessoa de emergência deve ter até 100 caracteres")
        String pessoaEmerg,

        String contatoEmerg
) {
    public String apelidoNormalizado() {
        return apelido == null || apelido.isBlank() ? null : apelido.trim();
    }

    public String fotoPerfilNormalizado() {
        return fotoPerfil == null || fotoPerfil.isBlank() ? null : fotoPerfil.trim();
    }

    public String cpfNormalizado() {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }
        return cpf.replaceAll("\\D", "");
    }

    public String pessoaEmergNormalizado() {
        return pessoaEmerg == null || pessoaEmerg.isBlank() ? null : pessoaEmerg.trim();
    }

    public String contatoEmergNormalizado() {
        if (contatoEmerg == null || contatoEmerg.isBlank()) {
            return null;
        }
        return contatoEmerg.replaceAll("\\D", "");
    }
}