package dev.krtechs.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.krtechs.model.Estabelecimento;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Integer> {

    @Query("SELECT est FROM Estabelecimento est WHERE est.createdAt BETWEEN :startDate AND :endDate ORDER BY est.id DESC")
    List<Estabelecimento> findByCreatedAtEstabelecimentos(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);;

    @Query("SELECT est from Estabelecimento est WHERE est.id in :ids")
    List<Estabelecimento> findByIDsEstabelecimentos(@Param("ids") List<Integer> estabelecimentoIDs);
}