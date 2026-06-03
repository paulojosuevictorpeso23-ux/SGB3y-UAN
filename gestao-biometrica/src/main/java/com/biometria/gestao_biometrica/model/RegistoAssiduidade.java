package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registos_assiduidade")
public class RegistoAssiduidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    @Column(name = "data_hora", updatable = false)
    private LocalDateTime dataHora;

    @Column(name = "tipo_movimento", nullable = false, length = 10)
    private String tipoMovimento; 

    @Column(name = "dispositivo_origem", length = 50)
    private String dispositivoOrigem;

    // --- CONSTRUTORES OBRIGATÓRIOS PARA O HIBERNATE ---
    public RegistoAssiduidade() {
    }

    public RegistoAssiduidade(Long id, Utilizador utilizador, String tipoMovimento, String dispositivoOrigem) {
        this.id = id;
        this.utilizador = utilizador;
        this.tipoMovimento = tipoMovimento;
        this.dispositivoOrigem = dispositivoOrigem;
    }

    @PrePersist
    protected void onCreate() {
        this.dataHora = LocalDateTime.now();
    }

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilizador getUtilizador() { return utilizador; }
    public void setUtilizador(Utilizador utilizador) { this.utilizador = utilizador; }

    public LocalDateTime getDataHora() { return dataHora; }

    public String getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(String tipoMovimento) { this.tipoMovimento = tipoMovimento; }

    public String getDispositivoOrigem() { return dispositivoOrigem; }
    public void setDispositivoOrigem(String dispositivoOrigem) { this.dispositivoOrigem = dispositivoOrigem; }
}
