package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    Optional<Utilizador> findByUsername(String username);
    Optional<Utilizador> findByNome(String nome);

    // ==========================================
    // METODO ADICIONADO PARA O DASHBOARD
    // ==========================================
    // Isto diz ao Spring para fazer: 
    // SELECT COUNT(*) FROM utilizadores u JOIN cargos c ON u.cargo_id = c.id WHERE c.nome = ?
    long countByCargoNome(String nomeCargo);
}
