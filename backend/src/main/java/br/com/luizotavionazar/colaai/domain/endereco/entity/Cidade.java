package br.com.luizotavionazar.colaai.domain.endereco.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cidades")
public class Cidade {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "codIbge", nullable = false, updatable = false)
    private Integer codIbge;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @CreationTimestamp
    @Column(name = "dataCriacao", updatable = false)
    private LocalDateTime dataCriacao;
}