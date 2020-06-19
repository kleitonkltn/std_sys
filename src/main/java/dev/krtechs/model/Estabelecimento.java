package dev.krtechs.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("id")
    private Integer id;

    @Column(nullable = false, length = 18)
    @NotNull(message = "{campo.cnpj.obrigatorio}")
    private String cnpj_cpf;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "{campo.razao.obrigatorio}")
    private String razao_social;
    
    private String nome_fantasia;
    private String endereco;
    private String contato;
    private String bairro;
    private String UF;
    private String municipio;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String tipo_de_risco;
    @Column(columnDefinition = "TEXT")
    private String grau_de_risco;
    @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt_BR")
    @Column(updatable = false)
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt_BR")
    private LocalDate updatedAt;

    @PrePersist
    public void PrePersist() {
        setCreatedAt(LocalDate.now());
    }

    @PreUpdate
    public void PreUpdate() {
        setUpdatedAt(LocalDate.now());
    }
    
}

