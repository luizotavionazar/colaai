package br.com.luizotavionazar.colaai.domain.configuracao.repository;

import br.com.luizotavionazar.colaai.domain.configuracao.entity.ConfiguracaoAplicacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoAplicacaoRepository extends JpaRepository<ConfiguracaoAplicacao, Long> {
}