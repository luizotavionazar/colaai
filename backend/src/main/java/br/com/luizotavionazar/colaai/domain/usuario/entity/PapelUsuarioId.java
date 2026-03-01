package br.com.luizotavionazar.colaai.domain.usuario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PapelUsuarioId implements Serializable {
    @Column(name = "idUsuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "idPapel", nullable = false)
    private Integer idPapel;
}