package br.com.luizotavionazar.colaai.domain.usuario.repository;

import br.com.luizotavionazar.colaai.domain.usuario.entity.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PapelRepository extends JpaRepository<Papel, Integer> {

    Optional<Papel> findByPapel(String papel);

    boolean existsByPapel(String papel);
}