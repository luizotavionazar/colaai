package br.com.luizotavionazar.colaai.domain.autenticacao.service;

import br.com.luizotavionazar.colaai.domain.autenticacao.entity.PoliticaSenha;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PoliticaSenhaService {

    public void validar(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Senha é obrigatória"
            );
        }

        boolean tamanhoValido = senha.length() >= PoliticaSenha.MIN_CARACTERES
                                && senha.length() <= PoliticaSenha.MAX_CARACTERES;
            
        boolean possuiMaiuscula = senha.matches(".*\\p{Lu}.*");
        boolean possuiNumero = senha.matches(".*\\d.*");
        boolean possuiEspecial = senha.matches(".*[^\\p{L}\\d\\s].*");
            
        if (!tamanhoValido || !possuiMaiuscula || !possuiNumero || !possuiEspecial) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A senha deve ter entre 8 e 160 caracteres, incluindo pelo menos 1 letra maiúscula, 1 número e 1 caractere especial"
            );
        }
    }
}