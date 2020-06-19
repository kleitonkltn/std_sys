package dev.krtechs.rest.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

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
}