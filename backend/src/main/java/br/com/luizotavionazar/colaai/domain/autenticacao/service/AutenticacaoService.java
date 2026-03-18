package br.com.luizotavionazar.colaai.domain.autenticacao.service;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroResponse;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import br.com.luizotavionazar.colaai.domain.endereco.repository.CidadeRepository;
import br.com.luizotavionazar.colaai.domain.endereco.service.EnderecoService;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import br.com.luizotavionazar.colaai.domain.usuario.repository.UsuarioRepository;
import br.com.luizotavionazar.colaai.domain.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final CidadeRepository cidadeRepository;
    private final EnderecoService enderecoService;

    @Transactional
    public CadastroResponse cadastrar(CadastroRequest request) {
        String emailNormalizado = request.emailNormalizado();

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        validarTelefone(request);
        validarCidade(request);

        Cidade cidade = cidadeRepository.findById(request.codIbgeCidade())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada"));

        Usuario usuario = usuarioService.cadastrar(emailNormalizado, request.senha());

        Endereco endereco = enderecoService.cadastrarBasico(request.codIbgeCidade());

        // Próximo passo:
        // criar Pessoa vinculando:
        // - request.nomeNormalizado()
        // - request.telefoneNormalizado()
        // - request.sexoNormalizado()
        // - request.dataNascimento()
        // - usuario
        // - endereco

        return CadastroResponse.from(usuario, request, cidade);
    }

    private void validarTelefone(CadastroRequest request) {
        String telefone = request.telefoneNormalizado();

        if (telefone.length() < 10 || telefone.length() > 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
        }
    }

    private void validarCidade(CadastroRequest request) {
        if (request.codIbgeCidade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade é obrigatória");
        }
    }
}