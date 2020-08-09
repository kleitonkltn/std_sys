package dev.krtechs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public Estabelecimento save(@RequestBody @Valid Estabelecimento estabelecimento) {
        estabelecimento.setCnpj_cpf(estabelecimento.getCnpj_cpf().replaceAll("\\D", ""));
        return repository.save(estabelecimento);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public Estabelecimento getByID(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public List<Estabelecimento> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Integer id) {
        repository.findById(id).map(estabelecimento -> {
            repository.deleteById(id);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public void update(@PathVariable Integer id, @RequestBody @Valid Estabelecimento estabelecimentoUpdate) {
        repository.findById(id).map(estabelecimento -> {
            estabelecimentoUpdate.setCnpj_cpf(estabelecimentoUpdate.getCnpj_cpf().replaceAll("\\D", ""));
            return repository.save(estabelecimentoUpdate);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estabelecimento Não Encontrado"));
    }

}