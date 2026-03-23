package br.com.luizotavionazar.colaai.domain.autenticacao.repository;

import br.com.luizotavionazar.colaai.domain.autenticacao.entity.TokenRecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoSenhaRepository extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByTokenHash(String tokenHash);

    Optional<TokenRecuperacaoSenha> findFirstByUsuarioIdAndUsadoEmIsNullAndEncerradoEmIsNullOrderByDataCriacaoDesc(Integer usuarioId);
}