package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistoAssiduidadeRepository extends JpaRepository<RegistoAssiduidade, Long> {
    
    // Método adicionado para listar os pontos de um utilizador ordenados do mais recente para o mais antigo
    List<RegistoAssiduidade> findByUtilizadorOrderByDataHoraDesc(Utilizador utilizador);
}
