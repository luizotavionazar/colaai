package br.com.luizotavionazar.colaai.domain.usuario.repository;

import br.com.luizotavionazar.colaai.domain.usuario.entity.PapelUsuario;
import br.com.luizotavionazar.colaai.domain.usuario.entity.PapelUsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PapelUsuarioRepository extends JpaRepository<PapelUsuario, PapelUsuarioId> {
}