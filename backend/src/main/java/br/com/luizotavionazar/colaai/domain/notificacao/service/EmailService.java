package br.com.luizotavionazar.colaai.domain.notificacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public void enviarBoasVindas(String nome, String email) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(from);
        mensagem.setTo(email);
        mensagem.setSubject("Bem-vindo ao Cola Aí!");

        String corpo = """
                Olá, %s!

                Seu cadastro no Cola Aí foi realizado com sucesso.

                Agora você já pode acessar sua conta e completar seu perfil:
                %s/login

                Seja bem-vindo!
                """.formatted(nome, frontendBaseUrl);

        mensagem.setText(corpo);

        mailSender.send(mensagem);
    }

    public void enviarRecuperacaoSenha(String nome, String email, String token) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(from);
        mensagem.setTo(email);
        mensagem.setSubject("Recuperação de senha - Cola Aí");

        String link = frontendBaseUrl + "/redefinir-senha?token=" + token;

        String corpo = """
                Olá, %s!

                Recebemos um pedido para redefinir sua senha no Cola Aí.

                Use o link abaixo para cadastrar uma nova senha:
                %s

                Este link expira em 30 minutos e pode ser usado apenas uma vez.

                Se você não solicitou essa alteração, ignore este e-mail.
                """.formatted(nome, link);

        mensagem.setText(corpo);

        mailSender.send(mensagem);
    }
}