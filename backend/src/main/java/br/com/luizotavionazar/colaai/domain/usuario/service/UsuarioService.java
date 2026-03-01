package br.com.luizotavionazar.colaai.domain.usuario.service;

import br.com.luizotavionazar.colaai.domain.usuario.entity.*;
import br.com.luizotavionazar.colaai.domain.usuario.repository.PapelRepository;
import br.com.luizotavionazar.colaai.domain.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(String email, String senha) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("! E-mail já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .email(email)
                .senhaHash(passwordEncoder.encode(senha))
                .build();

        atribuirPapel(usuario, PapelService.CLIENTE);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario tornarOrganizador(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("! Usuário não encontrado"));

        atribuirPapel(usuario, PapelService.ORGANIZADOR);

        return usuarioRepository.save(usuario);
    }

    private void atribuirPapel(Usuario usuario, String nomePapel) {
        Papel papel = papelRepository.findByPapel(nomePapel)
                .orElseThrow(() -> new IllegalStateException("! Papel " + nomePapel + " não existe na tabela"));

        boolean jaTem = usuario.getPapeis().stream()
                .anyMatch(pu -> pu.getPapel().getId().equals(papel.getId()));

        if (jaTem) return;

        PapelUsuario link = PapelUsuario.builder()
                .id(new PapelUsuarioId(usuario.getId(), papel.getId()))
                .usuario(usuario)
                .papel(papel)
                .build();

        usuario.getPapeis().add(link);
    }
}