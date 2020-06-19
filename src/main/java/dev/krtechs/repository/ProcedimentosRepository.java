package dev.krtechs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import dev.krtechs.model.Procedimentos;

public interface ProcedimentosRepository extends JpaRepository<Procedimentos, Integer> {


    @Query("SELECT p FROM Procedimentos p WHERE p.estabelecimento.id = ?1 ORDER BY p.id DESC")
    List<Procedimentos> findByIdEstabelecimento(Integer id_estabelecimentos);

}