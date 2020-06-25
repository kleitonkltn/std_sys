package dev.krtechs.rest.dto;

import javax.validation.constraints.NotNull;

import dev.krtechs.core.exception.DateFormatterException;
import dev.krtechs.model.Estabelecimento;
import dev.krtechs.model.Procedimentos;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
public class ProcedimentosDTO {
    private Integer id;
    @NotNull(message = "Campo Descrição Obrigatório")
    private String description;
    private String comments;
    private String accomplishedAt;
    @NotNull(message = "Campo Estabelecimento Obrigatório")
    private Integer estabelecimento;

    public LocalDate getAccomplishedAtFormatted() {
        return LocalDate.parse(this.accomplishedAt, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public Procedimentos dtoToProcedimentos(ProcedimentosDTO dto, Estabelecimento estabelecimento) {
        try {
            final Procedimentos procedimentos = new Procedimentos();
            if (dto.getId() != null) {
                procedimentos.setId(dto.getId());
            }
            procedimentos.setComments(dto.getComments());
            procedimentos.setAccomplishedAt(dto.getAccomplishedAtFormatted());
            procedimentos.setEstabelecimento(estabelecimento);
            procedimentos.setDescription(dto.getDescription());
            return procedimentos;
        } catch (DateTimeParseException ex) {
            throw new DateFormatterException();

        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object Incorrect");
        }

    }

}





