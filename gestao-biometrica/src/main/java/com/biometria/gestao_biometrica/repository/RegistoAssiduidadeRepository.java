package com.biometria.gestao_biometrica.repository;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistoAssiduidadeRepository extends JpaRepository<RegistoAssiduidade, Long> {

    // Correção do Erro 1 (Controller): Conta utilizadores únicos num intervalo de tempo e tipo
    @Query("SELECT COUNT(DISTINCT r.utilizador) FROM RegistoAssiduidade r WHERE r.dataHora BETWEEN :inicio AND :fim AND r.tipo = :tipo")
    long countDistinctUtilizadoresByDataHoraBetweenAndTipo(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim, 
        @Param("tipo") String tipo
    );

    // Correção do Erro 2 (Service): Procura o histórico de assiduidade de um utilizador, do mais recente para o mais antigo
    List<RegistoAssiduidade> findByUtilizadorOrderByDataHoraDesc(Utilizador utilizador);

    // NOVO: Busca todos os registos do sistema entre duas datas para o Relatório Avançado
    List<RegistoAssiduidade> findByDataHoraBetweenOrderByDataHoraDesc(LocalDateTime inicio, LocalDateTime fim);
}

