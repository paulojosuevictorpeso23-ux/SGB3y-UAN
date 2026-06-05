package com.biometria.gestao_biometrica.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String palavraPasse;
}
