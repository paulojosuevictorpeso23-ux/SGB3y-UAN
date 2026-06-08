package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    // Para podermos procurar um cargo pelo nome (ex: "ADMIN") mais tarde
    Optional<Cargo> findByNome(String nome);
}
