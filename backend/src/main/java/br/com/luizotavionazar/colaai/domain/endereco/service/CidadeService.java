package br.com.luizotavionazar.colaai.domain.endereco.service;

import br.com.luizotavionazar.colaai.api.endereco.dto.CidadeResponse;
import br.com.luizotavionazar.colaai.domain.endereco.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    @Transactional(readOnly = true)
    public List<CidadeResponse> listarCidades() {
        return cidadeRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(CidadeResponse::from)
                .toList();
    }
}