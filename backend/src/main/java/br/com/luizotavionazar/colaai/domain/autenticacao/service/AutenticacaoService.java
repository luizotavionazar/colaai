package br.com.luizotavionazar.colaai.domain.autenticacao.service;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.CadastroResponse;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.LoginRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import br.com.luizotavionazar.colaai.domain.endereco.service.EnderecoService;
import br.com.luizotavionazar.colaai.domain.endereco.repository.CidadeRepository;
import br.com.luizotavionazar.colaai.domain.pessoa.service.PessoaService;
import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import br.com.luizotavionazar.colaai.domain.usuario.repository.UsuarioRepository;
import br.com.luizotavionazar.colaai.domain.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.com.luizotavionazar.colaai.domain.autenticacao.event.UsuarioCadastradoEvent;
import br.com.luizotavionazar.colaai.domain.autenticacao.repository.TokenRecuperacaoSenhaRepository;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.MensagemResponse;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.RecuperacaoSenhaRequest;
import br.com.luizotavionazar.colaai.api.autenticacao.dto.RedefinirSenhaRequest;
import br.com.luizotavionazar.colaai.domain.autenticacao.entity.TokenRecuperacaoSenha;
import br.com.luizotavionazar.colaai.domain.notificacao.service.EmailService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.context.ApplicationEventPublisher;
import br.com.luizotavionazar.colaai.config.security.JwtService;
import br.com.luizotavionazar.colaai.domain.pessoa.repository.PessoaRepository;
import br.com.luizotavionazar.colaai.domain.autenticacao.entity.ControleRecuperacaoSenha;
import br.com.luizotavionazar.colaai.domain.autenticacao.repository.ControleRecuperacaoSenhaRepository;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final CidadeRepository cidadeRepository;
    private final PessoaRepository pessoaRepository;
    private final PessoaService pessoaService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;
    private final EmailService emailService;
    private final ControleRecuperacaoSenhaRepository controleRecuperacaoSenhaRepository;

    private static final long COOLDOWN_TOKEN_MINUTES = 2;
    private static final long EXPIRACAO_TOKEN_MINUTES = 30;
    private static final long JANELA_IP_MINUTES = 10;
    private static final int LIMITE_TENTATIVAS_IP = 5;
    private static final long BLOQUEIO_IP_MINUTES = 10;

    private static final String MOTIVO_SUBSTITUIDO = "SUBSTITUIDO";
    private static final String MOTIVO_EXPIRADO = "EXPIRADO";
    private static final String MOTIVO_UTILIZADO = "UTILIZADO";

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

        pessoaService.cadastrarBasico(request, usuario, endereco);

        eventPublisher.publishEvent(
                new UsuarioCadastradoEvent(
                        usuario.getId(),
                        request.nomeNormalizado(),
                        usuario.getEmail()
                )
        );

        return CadastroResponse.from(usuario, request, cidade);
    }

    private void validarCidade(CadastroRequest request) {
        if (request.codIbgeCidade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade é obrigatória");
        }

        if (!cidadeRepository.existsByCodIbge(request.codIbgeCidade())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade não encontrada");
        }
    }

    private void validarTelefone(CadastroRequest request) {
        String telefone = request.telefoneNormalizado();

        if (telefone.length() < 10 || telefone.length() > 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String emailNormalizado = request.emailNormalizado();
    
        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha incorretos"
                ));
    
        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha incorretos"
            );
        }
    
        String token = jwtService.gerarToken(usuario);
    
        return LoginResponse.from(usuario, token, jwtService.getExpirationMinutes());
    }

    @Transactional
    public MensagemResponse iniciarRecuperacaoSenha(RecuperacaoSenhaRequest request, String ip) {
        if (ipBloqueadoOuRegistrarTentativa(ip)) {
            return mensagemGenericaRecuperacao();
        }

        String emailNormalizado = request.emailNormalizado();

        usuarioRepository.findByEmail(emailNormalizado).ifPresent(usuario -> {
            LocalDateTime agora = LocalDateTime.now();

            tokenRecuperacaoSenhaRepository
                    .findFirstByUsuarioIdAndUsadoEmIsNullAndInvalidadoEmIsNullOrderByDataCriacaoDesc(usuario.getId())
                    .ifPresent(tokenAnterior -> {
                        if (!tokenAnterior.expirado()) {
                            if (agora.isBefore(tokenAnterior.getDataCriacao().plusMinutes(COOLDOWN_TOKEN_MINUTES))) {
                                return;
                            }

                            tokenAnterior.setInvalidadoEm(agora);
                            tokenAnterior.setMotivoEncerramento(MOTIVO_SUBSTITUIDO);
                            return;
                        }

                        tokenAnterior.setInvalidadoEm(agora);
                        tokenAnterior.setMotivoEncerramento(MOTIVO_EXPIRADO);
                    });

            boolean aindaExisteTokenAtivo = tokenRecuperacaoSenhaRepository
                    .findFirstByUsuarioIdAndUsadoEmIsNullAndInvalidadoEmIsNullOrderByDataCriacaoDesc(usuario.getId())
                    .filter(token -> !token.expirado())
                    .isPresent();

            if (aindaExisteTokenAtivo) {
                return;
            }

            String tokenBruto = gerarTokenSeguro();
            String tokenHash = gerarHash(tokenBruto);

            TokenRecuperacaoSenha token = TokenRecuperacaoSenha.builder()
                    .usuario(usuario)
                    .tokenHash(tokenHash)
                    .expiraEm(agora.plusMinutes(EXPIRACAO_TOKEN_MINUTES))
                    .ipSolicitacao(ip)
                    .build();

            tokenRecuperacaoSenhaRepository.save(token);

            String nome = pessoaRepository.findByUsuarioId(usuario.getId())
                    .map(pessoa -> pessoa.getNome())
                    .orElse("usuário");

            emailService.enviarRecuperacaoSenha(nome, usuario.getEmail(), tokenBruto);
        });

        return mensagemGenericaRecuperacao();
    }
    
    @Transactional
    public MensagemResponse redefinirSenha(RedefinirSenhaRequest request) {
        String tokenHash = gerarHash(request.token());
    
        TokenRecuperacaoSenha token = tokenRecuperacaoSenhaRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Link de recuperação inválido ou expirado"
                ));
    
        if (token.usado() || token.expirado() || token.invalidado()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Link de recuperação inválido ou expirado"
            );
        }
    
        validarNovaSenha(request.novaSenha());
    
        Usuario usuario = token.getUsuario();

        if (passwordEncoder.matches(request.novaSenha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A nova senha deve ser diferente da atual"
            );
        }

        LocalDateTime agora = LocalDateTime.now();

        usuario.setSenhaHash(passwordEncoder.encode(request.novaSenha()));

        token.setUsadoEm(agora);
        token.setInvalidadoEm(agora);
        token.setMotivoEncerramento(MOTIVO_UTILIZADO);

        return new MensagemResponse("Senha redefinida com sucesso");
    }
    
    @Transactional(readOnly = true)
    public MensagemResponse validarTokenRecuperacao(String tokenBruto) {
        String tokenHash = gerarHash(tokenBruto);
    
        TokenRecuperacaoSenha token = tokenRecuperacaoSenhaRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Link de recuperação inválido ou expirado"
                ));
    
        if (token.usado() || token.expirado() || token.invalidado()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Link de recuperação inválido ou expirado"
            );
        }
    
        return new MensagemResponse("Token válido");
    }
    
    private void validarNovaSenha(String senha) {
        boolean tamanho = senha != null && senha.length() >= 8;
        boolean letra = senha != null && senha.matches(".*[A-Za-z].*");
        boolean numero = senha != null && senha.matches(".*\\d.*");
    
        if (!(tamanho && letra && numero)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A nova senha deve ter pelo menos 8 caracteres, incluindo letras e números"
            );
        }
    }
    
    private String gerarTokenSeguro() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    private String gerarHash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar hash do token", ex);
        }
    }

    private MensagemResponse mensagemGenericaRecuperacao() {
        return new MensagemResponse(
                "Se encontrarmos uma conta vinculada a esse e-mail, enviaremos as instruções de recuperação."
        );
    }

    private boolean ipBloqueadoOuRegistrarTentativa(String ip) {
        LocalDateTime agora = LocalDateTime.now();

        ControleRecuperacaoSenha controle = controleRecuperacaoSenhaRepository.findByIp(ip)
                .orElseGet(() -> ControleRecuperacaoSenha.builder()
                        .ip(ip)
                        .janelaInicio(agora)
                        .quantidade(0)
                        .build());

        if (controle.getBloqueadoAte() != null && agora.isBefore(controle.getBloqueadoAte())) {
            return true;
        }

        if (controle.getJanelaInicio() == null || agora.isAfter(controle.getJanelaInicio().plusMinutes(JANELA_IP_MINUTES))) {
            controle.setJanelaInicio(agora);
            controle.setQuantidade(1);
            controle.setBloqueadoAte(null);
            controleRecuperacaoSenhaRepository.save(controle);
            return false;
        }

        int novaQuantidade = controle.getQuantidade() + 1;
        controle.setQuantidade(novaQuantidade);

        if (novaQuantidade > LIMITE_TENTATIVAS_IP) {
            controle.setBloqueadoAte(agora.plusMinutes(BLOQUEIO_IP_MINUTES));
            controleRecuperacaoSenhaRepository.save(controle);
            return true;
        }

        controleRecuperacaoSenhaRepository.save(controle);
        return false;
    }
}