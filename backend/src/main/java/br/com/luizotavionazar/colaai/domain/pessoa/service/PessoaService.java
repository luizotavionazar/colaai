package br.com.luizotavionazar.colaai.domain.pessoa.service;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroRequest;
import br.com.luizotavionazar.colaai.api.pessoa.dto.PessoaRequest;
import br.com.luizotavionazar.colaai.api.pessoa.dto.PessoaResponse;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import br.com.luizotavionazar.colaai.domain.pessoa.entity.Pessoa;
import br.com.luizotavionazar.colaai.domain.pessoa.repository.PessoaRepository;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Transactional
    public Pessoa cadastrarBasico(CadastroRequest request, Usuario usuario, Endereco endereco) {
        if (pessoaRepository.existsByUsuarioId(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma pessoa para este usuário");
        }

        validarTelefone(request.telefoneNormalizado());

        Pessoa pessoa = Pessoa.builder()
                .usuario(usuario)
                .endereco(endereco)
                .nome(request.nomeNormalizado())
                .telefone(request.telefoneNormalizado())
                .sexo(request.sexoNormalizado())
                .dataNascimento(request.dataNascimento())
                .build();

        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public PessoaResponse completarCadastro(Integer pessoaId, PessoaRequest request) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        Integer idPessoaAtual = pessoa.getId();

        validarContatoEmerg(request.contatoEmergNormalizado());

        String cpfHash = gerarCpfHash(request.cpfNormalizado());

        if (cpfHash != null) {
            pessoaRepository.findByCpfHash(cpfHash)
                    .filter(outraPessoa -> !outraPessoa.getId().equals(idPessoaAtual))
                    .ifPresent(outraPessoa -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
                    });
        }

        pessoa.setApelido(request.apelidoNormalizado());
        pessoa.setFotoPerfil(request.fotoPerfilNormalizado());
        pessoa.setCpfHash(cpfHash);
        pessoa.setPessoaEmerg(request.pessoaEmergNormalizado());
        pessoa.setContatoEmerg(request.contatoEmergNormalizado());

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        return PessoaResponse.from(pessoaSalva);
    }

    @Transactional(readOnly = true)
    public PessoaResponse buscarPorId(Integer id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        return PessoaResponse.from(pessoa);
    }

    @Transactional(readOnly = true)
    public PessoaResponse buscarPoridUsuario(Integer idUsuario) {
        Pessoa pessoa = pessoaRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pessoa não encontrada para este usuário"));

        return PessoaResponse.from(pessoa);
    }

    private void validarTelefone(String telefone) {
        if (telefone.length() < 10 || telefone.length() > 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
        }
    }

    private void validarContatoEmerg(String contatoEmerg) {
        if (contatoEmerg != null && (contatoEmerg.length() < 10 || contatoEmerg.length() > 11)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contato de emergência inválido");
        }
    }

    private String gerarCpfHash(String cpfNormalizado) {
        if (cpfNormalizado == null) {
            return null;
        }

        if (cpfNormalizado.length() != 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(cpfNormalizado.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Algoritmo de hash não disponível", ex);
        }
    }

    @Transactional(readOnly = true)
    public PessoaResponse buscarMinhaPessoa(Integer idUsuario) {
        Pessoa pessoa = pessoaRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pessoa não encontrada para o usuário autenticado"
                ));
    
        return PessoaResponse.from(pessoa);
    }
}