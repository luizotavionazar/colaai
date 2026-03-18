package br.com.luizotavionazar.colaai.domain.endereco.service;

import br.com.luizotavionazar.colaai.api.endereco.dto.EnderecoRequest;
import br.com.luizotavionazar.colaai.api.endereco.dto.EnderecoResponse;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Cidade;
import br.com.luizotavionazar.colaai.domain.endereco.entity.Endereco;
import br.com.luizotavionazar.colaai.domain.endereco.repository.CidadeRepository;
import br.com.luizotavionazar.colaai.domain.endereco.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final CidadeRepository cidadeRepository;

    @Transactional
    public EnderecoResponse cadastrarCompleto(EnderecoRequest request) {
        Endereco endereco = salvarCompleto(request);
        return EnderecoResponse.from(endereco);
    }

    @Transactional
    public Endereco cadastrarBasico(Integer codIbgeCidade) {
        validarCidade(codIbgeCidade);

        Cidade cidade = buscarCidadeObrigatoria(codIbgeCidade);

        Endereco endereco = Endereco.builder()
                .cidade(cidade)
                .build();

        return enderecoRepository.save(endereco);
    }

    @Transactional
    protected Endereco salvarCompleto(EnderecoRequest request) {
        validarCidade(request.idCidade());
        validarCep(request.cepNormalizado());

        Cidade cidade = buscarCidadeObrigatoria(request.idCidade());

        Endereco endereco = Endereco.builder()
                .cidade(cidade)
                .cep(request.cepNormalizado())
                .logradouro(request.logradouroNormalizado())
                .bairro(request.bairroNormalizado())
                .numero(request.numeroNormalizado())
                .complemento(request.complementoNormalizado())
                .build();

        return enderecoRepository.save(endereco);
    }

    private Cidade buscarCidadeObrigatoria(Integer idCidade) {
        return cidadeRepository.findById(idCidade)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cidade não encontrada"
                ));
    }

    private void validarCidade(Integer idCidade) {
        if (idCidade == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cidade é obrigatória");
        }
    }

    private void validarCep(String cep) {
        if (cep != null && cep.length() != 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
        }
    }
}