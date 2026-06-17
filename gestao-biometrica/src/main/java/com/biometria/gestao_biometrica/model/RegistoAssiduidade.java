package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    private LocalDateTime dataHora;
    private String tipo; 
    private String estadoPonto; // Certifica-te de que o nome é este (o teu serviço usa estadoPonto)
}

