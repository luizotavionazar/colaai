package br.com.luizotavionazar.colaai.domain.autenticacao.listener;

import br.com.luizotavionazar.colaai.domain.autenticacao.event.UsuarioCadastradoEvent;
import br.com.luizotavionazar.colaai.domain.notificacao.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioCadastradoListener {

    private final EmailService emailService;

    @TransactionalEventListener
    public void aoCadastrarUsuario(UsuarioCadastradoEvent event) {
        try {
            emailService.enviarBoasVindas(event.nome(), event.email());
        } catch (Exception ex) {
            log.error("Falha ao enviar e-mail de boas-vindas para {}", event.email(), ex);
        }
    }
}