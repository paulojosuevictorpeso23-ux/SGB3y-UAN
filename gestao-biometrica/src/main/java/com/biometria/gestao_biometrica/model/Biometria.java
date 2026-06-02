package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometrias")
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizador;

    @Column(name = "sensor_template_id", nullable = false)
    private Integer sensorTemplateId;

    @Column(name = "data_registo", updatable = false)
    private LocalDateTime dataRegisto;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        this.dataRegisto = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilizador getUtilizador() { return utilizador; }
    public void setUtilizador(Utilizador utilizador) { this.utilizador = utilizador; }

    public Integer getSensorTemplateId() { return sensorTemplateId; }
    public void setSensorTemplateId(Integer sensorTemplateId) { this.sensorTemplateId = sensorTemplateId; }

    public LocalDateTime getDataRegisto() { return dataRegisto; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
