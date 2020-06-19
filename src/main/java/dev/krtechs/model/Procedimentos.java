package dev.krtechs.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
public class Procedimentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt_BR")
    private LocalDate accomplishedAt;

    @ManyToOne
    @JoinColumn(name = "estabelecimento_id")
    private Estabelecimento estabelecimento;

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
