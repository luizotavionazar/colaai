package br.com.luizotavionazar.colaai.domain.endereco.repository;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    boolean existsByCodIbge(Integer codIbge);
    List<Cidade> findAllByOrderByNomeAsc();
}