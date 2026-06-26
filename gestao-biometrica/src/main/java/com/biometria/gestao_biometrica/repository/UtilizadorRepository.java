package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List; // <--- ADICIONE ESTA LINHA AQUI

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    Optional<Utilizador> findByUsername(String username);
    Optional<Utilizador> findByNome(String nome);

    long countByCargoNome(String nomeCargo);

    // Agora o Java reconhece o que é uma "List"
    List<Utilizador> findByEstado(String estado); 
}

