package br.com.luizotavionazar.colaai.domain.endereco.repository;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}