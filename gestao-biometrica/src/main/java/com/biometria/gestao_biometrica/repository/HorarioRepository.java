package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
}
