package dev.krtechs.controllers;

import java.util.List;


import org.springframework.data.domain.Sort;
import javax.persistence.OrderBy;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import dev.krtechs.model.Estabelecimento;
import dev.krtechs.repository.EstabelecimentoRepository;

@RestController
@RequestMapping("/api/estabelecimentos")
public class EstabelecimentoController {
    private final EstabelecimentoRepository repository;

    @Autowired
    public EstabelecimentoController(final EstabelecimentoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Estabelecimento save(@RequestBody @Valid Estabelecimento estabelecimento) {
        return repository.save(estabelecimento);
    }

    @GetMapping("{id}")
    public Estabelecimento getByID(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }

    @GetMapping
    @OrderBy("id")
    public List<Estabelecimento> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.findById(id).map(estabelecimento -> {
            repository.deleteById(id);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable Integer id, @RequestBody @Valid Estabelecimento estabelecimentoUpdate) {
        repository.findById(id).map(estabelecimento -> {
            return repository.save(estabelecimentoUpdate);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }
}