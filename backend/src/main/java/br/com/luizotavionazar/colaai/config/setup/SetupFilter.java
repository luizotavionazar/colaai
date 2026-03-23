package br.com.luizotavionazar.colaai.config.setup;

import br.com.luizotavionazar.colaai.api.autenticacao.dto.MensagemResponse;
import br.com.luizotavionazar.colaai.domain.configuracao.service.SetupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetupFilter extends OncePerRequestFilter {

    private static final Set<String> PATHS_LIBERADOS = Set.of(
            "/setup",
            "/setup/status",
            "/error"
    );

    private final SetupService setupService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return HttpMethod.OPTIONS.matches(request.getMethod())
                || PATHS_LIBERADOS.contains(path)
                || path.startsWith("/setup/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!setupService.setupConcluido()) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(
                    response.getOutputStream(),
                    new MensagemResponse("A aplicação precisa ser configurada antes do uso.")
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}