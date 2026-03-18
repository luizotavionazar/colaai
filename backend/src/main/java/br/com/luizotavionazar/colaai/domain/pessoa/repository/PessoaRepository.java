package br.com.luizotavionazar.colaai.domain.pessoa.repository;

import br.com.luizotavionazar.colaai.domain.pessoa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    boolean existsByUsuarioId(Integer idUsuario);

    Optional<Pessoa> findByUsuarioId(Integer idUsuario);

    Optional<Pessoa> findByUsuarioEmail(String email);

    boolean existsByCpfHash(String cpfHash);

    Optional<Pessoa> findByCpfHash(String cpfHash);
}