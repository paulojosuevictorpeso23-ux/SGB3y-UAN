package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    
    // Esta linha permite-nos procurar um funcionário/estudante pelo número da UAN muito facilmente no futuro
    Optional<Utilizador> findByIdentificacaoEscolar(String identificacaoEscolar);
}
