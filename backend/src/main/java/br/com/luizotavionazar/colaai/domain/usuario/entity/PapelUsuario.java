package br.com.luizotavionazar.colaai.domain.usuario.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "papelUsuario")
public class PapelUsuario {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private PapelUsuarioId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idUsuario")
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @MapsId("idPapel")
    @JoinColumn(name = "idPapel", nullable = false)
    private Papel papel;

    @CreationTimestamp
    @Column(name = "dataCriacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dataAtualiza")
    private LocalDateTime dataAtualiza;
}