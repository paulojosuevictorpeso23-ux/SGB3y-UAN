package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometrias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: Muitas biometrias pertencem a Um utilizador
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    // Força o PostgreSQL a criar a coluna como TEXT (ideal para o vetor hexadecimal)
    @Column(name = "template_biometrico", nullable = false, columnDefinition = "TEXT")
    private String templateBiometrico;

    @Column(name = "dedo_registado", nullable = false, length = 30)
    private String dedoRegistado; // Ex: "POLEGAR_DIREITO", "INDICADOR_ESQUERDO"

    @Column(name = "data_registo", nullable = false, updatable = false)
    private LocalDateTime dataRegisto;

    // Define automaticamente a data e hora do registo da digital
    @PrePersist
    protected void onCreate() {
        this.dataRegisto = LocalDateTime.now();
    }
}
