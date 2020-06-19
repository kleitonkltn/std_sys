package dev.krtechs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.krtechs.model.Estabelecimento;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Integer> {

}