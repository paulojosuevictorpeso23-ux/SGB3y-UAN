package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    Optional<Utilizador> findByUsername(String username);
}
