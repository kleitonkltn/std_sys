package dev.krtechs.controllers;

import java.util.List;

import javax.persistence.OrderBy;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.krtechs.model.Estabelecimento;
import dev.krtechs.model.Procedimentos;
import dev.krtechs.repository.ProcedimentosRepository;
import dev.krtechs.rest.dto.ProcedimentosDTO;
import dev.krtechs.services.EstabelecimentosService;

@RestController
@RequestMapping("/api/procedimentos")
public class ProcedimentosController {
    private final ProcedimentosRepository repository;
    
    private  final EstabelecimentosService estabelecimentosService;

    public ProcedimentosController(ProcedimentosRepository repository,
                                   EstabelecimentosService estabelecimentosService) {
        this.repository = repository;
        this.estabelecimentosService = estabelecimentosService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public Procedimentos save(@RequestBody @Valid final ProcedimentosDTO dto) {
        Estabelecimento estabelecimento = estabelecimentosService.verifyExists(dto.getEstabelecimento());
        final Procedimentos procedimentos = dto.dtoToProcedimentos(dto, estabelecimento);
        return repository.save(procedimentos);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public Procedimentos getByID(@PathVariable final Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }

    @GetMapping
    @OrderBy("id")
    @PreAuthorize("hasRole('ROLE_PUBLIC') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public List<Procedimentos> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @RequestMapping(value = "/filter/estabelecimento", method = RequestMethod.GET)
    public List<Procedimentos> getByIdEstabelecimento(@RequestParam(value = "id", required = true) final Integer id) {
        return repository.findByIdEstabelecimento(id);
    }
    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERVISOR')")
    public void update(@PathVariable final Integer id, @RequestBody @Valid final ProcedimentosDTO dto) {
        repository.findById(id).map(procedimentosMap -> {
            Estabelecimento estabelecimento = estabelecimentosService.verifyExists(dto.getEstabelecimento());
            final Procedimentos procedimentos = dto.dtoToProcedimentos(dto, estabelecimento);
            return repository.save(procedimentos);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize(" hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable final Integer id) {
        repository.findById(id).map(Procedimemtos -> {
            repository.deleteById(id);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }






}