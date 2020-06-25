package dev.krtechs.repository;

import dev.krtechs.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByMatricula(String matricula);

}
