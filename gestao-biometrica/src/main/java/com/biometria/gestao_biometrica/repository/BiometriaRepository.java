package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BiometriaRepository extends JpaRepository<Biometria, Long> {

    // O que tu já tinhas: Útil para gestão/cadastro de dedos
    List<Biometria> findByUtilizadorId(Long utilizadorId);

    // O que precisamos agora: Útil para identificar quem pôs o dedo no leitor
    Optional<Biometria> findByTemplateBiometrico(String templateBiometrico);
}

