package br.com.luizotavionazar.colaai.config.security;

import br.com.luizotavionazar.colaai.domain.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration-minutes}")
    private long expirationMinutes;

    public String gerarToken(Usuario usuario) {
        Instant now = Instant.now();

        String papeis = usuario.getPapeis().stream()
                .map(link -> link.getPapel().getPapel())
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("colaai")
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("scope", papeis)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getExpirationMinutes() {
        return expirationMinutes;
    }
}