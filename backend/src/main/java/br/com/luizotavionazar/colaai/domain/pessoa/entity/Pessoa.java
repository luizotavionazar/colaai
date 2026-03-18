package br.com.luizotavionazar.colaai.domain.pessoa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "apelido", length = 100)
    private String apelido;

    @Column(name = "fotoPerfil", length = 255)
    private String fotoPerfil;

    @ToString.Exclude
    @Column(name = "cpfHash", length = 64)
    private String cpfHash;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 11, nullable = false)
    private String telefone;

    @Column(name = "pessoaEmerg", length = 100)
    private String pessoaEmerg;

    @Column(name = "contatoEmerg", length = 11)
    private String contatoEmerg;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "sexo", nullable = false, length = 1)
    private String sexo;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEndereco", nullable = false)
    private Endereco endereco;

    @CreationTimestamp
    @Column(name = "dataCriacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dataAtualiza")
    private LocalDateTime dataAtualiza;
}