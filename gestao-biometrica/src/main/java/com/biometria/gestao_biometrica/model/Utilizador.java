package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilizadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "palavra_passe", nullable = false)
    private String palavraPasse;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String contacto; // Opcional (pode ser null no banco)

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;

    // RETIFICADO: Ligação adicionada com sucesso para a gestão de horários e atrasos
    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @Column(nullable = false, length = 15)
    private String estado = "ATIVO";

    // Campos preparados para a futura lógica de recuperação de palavra-passe
    @Column(name = "token_recuperacao", length = 100)
    private String tokenRecuperacao;

    @Column(name = "data_expiracao_token")
    private LocalDateTime dataExpiracaoToken;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
