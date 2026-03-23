package br.com.luizotavionazar.colaai.api.autenticacao.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import br.com.luizotavionazar.colaai.domain.autenticacao.entity.PoliticaSenha;

public record CadastroRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(
                min = PoliticaSenha.MIN_CARACTERES,
                max = PoliticaSenha.MAX_CARACTERES,
                message = "A senha deve ter entre " + PoliticaSenha.MIN_CARACTERES
                        + " e " + PoliticaSenha.MAX_CARACTERES + " caracteres"
        )
        String senha,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "Sexo é obrigatório")
        @Pattern(
                regexp = "M|F",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Sexo deve ser M ou F"
        )
        String sexo,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Cidade é obrigatória")
        Integer codIbgeCidade
) {
    public String emailNormalizado() {
        return email == null ? null : email.trim().toLowerCase();
    }

    public String telefoneNormalizado() {
        return telefone == null ? "" : telefone.replaceAll("\\D", "");
    }

    public String sexoNormalizado() {
        return sexo == null ? null : sexo.trim().toUpperCase();
    }

    public String nomeNormalizado() {
        return nome == null ? null : nome.trim();
    }
}