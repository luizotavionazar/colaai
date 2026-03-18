package br.com.luizotavionazar.colaai.api.endereco.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(

        @NotNull(message = "A cidade é obrigatória")
        Integer idCidade,

        @Size(max = 8, message = "O CEP deve ter no máximo 8 caracteres")
        String cep,

        @Size(max = 120, message = "O logradouro deve ter até 120 caracteres")
        String logradouro,

        @Size(max = 100, message = "O bairro deve ter até 100 caracteres")
        String bairro,

        @Size(max = 20, message = "O número deve ter até 20 caracteres")
        String numero,

        @Size(max = 100, message = "O complemento deve ter até 100 caracteres")
        String complemento

) {
    public String cepNormalizado() {
        if (cep == null || cep.isBlank()) {
            return null;
        }
        return cep.replaceAll("\\D", "");
    }

    public String logradouroNormalizado() {
        return logradouro == null || logradouro.isBlank() ? null : logradouro.trim();
    }

    public String bairroNormalizado() {
        return bairro == null || bairro.isBlank() ? null : bairro.trim();
    }

    public String numeroNormalizado() {
        return numero == null || numero.isBlank() ? null : numero.trim();
    }

    public String complementoNormalizado() {
        return complemento == null || complemento.isBlank() ? null : complemento.trim();
    }
}