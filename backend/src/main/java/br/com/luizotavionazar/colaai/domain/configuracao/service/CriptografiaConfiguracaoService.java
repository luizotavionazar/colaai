package br.com.luizotavionazar.colaai.domain.configuracao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CriptografiaConfiguracaoService {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public CriptografiaConfiguracaoService(@Value("${app.setup.master-key}") String masterKey) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(masterKey.getBytes(StandardCharsets.UTF_8));
            this.secretKey = new SecretKeySpec(hash, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível inicializar a criptografia de configuração", e);
        }
    }

    public String criptografar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] criptografado = cipher.doFinal(valor.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + criptografado.length);
            buffer.put(iv);
            buffer.put(criptografado);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível criptografar a configuração", e);
        }
    }

    public String descriptografar(String valorCriptografado) {
        if (valorCriptografado == null || valorCriptografado.isBlank()) {
            return null;
        }

        try {
            byte[] dados = Base64.getDecoder().decode(valorCriptografado);
            ByteBuffer buffer = ByteBuffer.wrap(dados);

            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);

            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível descriptografar a configuração", e);
        }
    }
}