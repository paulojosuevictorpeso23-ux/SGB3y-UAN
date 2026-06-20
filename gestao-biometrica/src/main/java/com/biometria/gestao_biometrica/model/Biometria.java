package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometrias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Ativa o listener de auditoria
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    @Column(name = "template_biometrico", nullable = false, columnDefinition = "TEXT")
    private String templateBiometrico;

    @Column(name = "dedo_registado", nullable = false, length = 30)
    private String dedoRegistado;

    // A auditoria preenche isto automaticamente na criação
    @CreatedDate
    @Column(name = "data_registo", nullable = false, updatable = false)
    private LocalDateTime dataRegisto;

    // A auditoria preenche isto automaticamente em cada atualização
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // NOTA: Podes remover o método @PrePersist antigo, 
    // pois o @CreatedDate já faz esse trabalho de forma mais limpa.
}
