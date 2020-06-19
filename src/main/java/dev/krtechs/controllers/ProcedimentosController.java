package dev.krtechs.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.krtechs.model.Estabelecimento;
import dev.krtechs.model.Procedimentos;
import dev.krtechs.repository.EstabelecimentoRepository;
import dev.krtechs.repository.ProcedimentosRepository;
import dev.krtechs.rest.dto.ProcedimentosDTO;

@RestController
@RequestMapping("/api/procedimentos")
public class ProcedimentosController {
    private final ProcedimentosRepository repository;
    private final EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    public ProcedimentosController(final ProcedimentosRepository repository,
            final EstabelecimentoRepository estabelecimentoRepository) {
        this.repository = repository;
        this.estabelecimentoRepository = estabelecimentoRepository;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Procedimentos save(@RequestBody @Valid final ProcedimentosDTO dto) {
        final Procedimentos procedimentos = new Procedimentos();
        final LocalDate dataFormater = LocalDate.parse(dto.getAccomplishedAt(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        final Estabelecimento estabelecimento = estabelecimentoRepository.findById(dto.getEstabelecimento())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estabelecimento Não Encontrado"));
        procedimentos.setComments(dto.getComments());
        procedimentos.setAccomplishedAt(dataFormater);
        procedimentos.setEstabelecimento(estabelecimento);
        procedimentos.setDescription(dto.getDescription());
        return repository.save(procedimentos);
    }

    @GetMapping("{id}")
    public Procedimentos getByID(@PathVariable final Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }

    @GetMapping
    @OrderBy("id")
    public List<Procedimentos> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @RequestMapping(value = "/filter/estabelecimento", method = RequestMethod.GET)
    public List<Procedimentos> getByIdEstabelecimento(@RequestParam(value = "id", required = true) final Integer id) {
        return repository.findByIdEstabelecimento(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Integer id) {
        repository.findById(id).map(Procedimemtos -> {
            repository.deleteById(id);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable final Integer id, @RequestBody @Valid final ProcedimentosDTO dto) {
        repository.findById(id).map(procedimentosMap -> {
            final Procedimentos procedimentos = new Procedimentos();
            final LocalDate dataFormater = LocalDate.parse(dto.getAccomplishedAt(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            final Estabelecimento estabelecimento = estabelecimentoRepository.findById(dto.getEstabelecimento())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Estabelecimento Não Encontrado"));
            procedimentos.setId(dto.getId());
            procedimentos.setComments(dto.getComments());
            procedimentos.setAccomplishedAt(dataFormater);
            procedimentos.setEstabelecimento(estabelecimento);
            procedimentos.setDescription(dto.getDescription());

            return repository.save(procedimentos);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedimento Não Encontrado"));
    }
}