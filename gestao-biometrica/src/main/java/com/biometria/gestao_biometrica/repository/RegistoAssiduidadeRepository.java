package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistoAssiduidadeRepository extends JpaRepository<RegistoAssiduidade, Long> {

    // Isto vai ser excelente para o teu projeto listar o histórico de picagens de um funcionário específico
    List<RegistoAssiduidade> findByUtilizadorId(Long utilizadorId);
}
