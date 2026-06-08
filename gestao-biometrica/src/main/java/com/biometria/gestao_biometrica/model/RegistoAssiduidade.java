package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "registos_assiduidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistoAssiduidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: Muitos registos de assiduidade pertencem a Um utilizador
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false, length = 10)
    private String tipo; // Guardará "ENTRADA" ou "SAIDA"

    @Column(name = "estado_ponto", length = 20)
    private String estadoPonto; // Guardará "A_TEMPO", "ATRASADO", "SAIDA_NORMAL" ou "SAIDA_ANTECIPADA"

    // Se não passarmos uma hora específica, o Java assume o segundo exato da picagem
    @PrePersist
    protected void onCreate() {
        if (this.dataHora == null) {
            this.dataHora = LocalDateTime.now();
        }
    }
}
