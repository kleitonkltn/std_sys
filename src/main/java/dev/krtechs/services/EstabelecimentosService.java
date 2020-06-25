package dev.krtechs.services;

import dev.krtechs.model.Estabelecimento;
import dev.krtechs.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EstabelecimentosService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    public Estabelecimento verifyExists(Integer id) {
        final Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estabelecimento Não Encontrado"));
        return estabelecimento;
    }
}