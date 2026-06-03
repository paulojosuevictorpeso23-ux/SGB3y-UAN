package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BiometriaRepository extends JpaRepository<Biometria, Long> {

    // Isto vai permitir-nos procurar todas as biometrias (dedos) cadastradas para um utilizador específico
    List<Biometria> findByUtilizadorId(Long utilizadorId);
}
