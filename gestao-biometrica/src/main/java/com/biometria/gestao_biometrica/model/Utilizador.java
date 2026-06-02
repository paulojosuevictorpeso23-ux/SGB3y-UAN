package com.biometria.gestao_biometrica.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilizadores")
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "identificacao_escolar", nullable = false, unique = true, length = 20)
    private String identificacaoEscolar;

    @Column(nullable = false, length = 50)
    private String cargo;

    @Column(length = 15)
    private String estado = "ATIVO";

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

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdentificacaoEscolar() { return identificacaoEscolar; }
    public void setIdentificacaoEscolar(String identificacaoEscolar) { this.identificacaoEscolar = identificacaoEscolar; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
