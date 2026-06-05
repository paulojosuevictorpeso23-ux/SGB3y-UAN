package com.biometria.gestao_biometrica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaRequest {
    private String username;
    private String senhaAntiga;
    private String senhaNova;
}
