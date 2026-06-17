package com.biometria.gestao_biometrica.dto;

public class PontoOnlineDTO {
    private String identificador;
    private String tipo;
    private String origem;
    private String dataHora;

    // Construtor Padrão
    public PontoOnlineDTO() {}

    // Construtor Completo
    public PontoOnlineDTO(String identificador, String tipo, String origem, String dataHora) {
        this.identificador = identificador;
        this.tipo = tipo;
        this.origem = origem;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public String getIdentificador() { 
        return identificador; 
    }
    public void setIdentificador(String identificador) { 
        this.identificador = identificador; 
    }

    public String getTipo() { 
        return tipo; 
    }
    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }

    public String getOrigem() { 
        return origem; 
    }
    public void setOrigem(String origem) { 
        this.origem = origem; 
    }

    public String getDataHora() { 
        return dataHora; 
    }
    public void setDataHora(String dataHora) { 
        this.dataHora = dataHora; 
    }
}

