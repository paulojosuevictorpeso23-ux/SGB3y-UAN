package com.biometria.gestao_biometrica;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OlaController {
    @GetMapping("/ola")
    public String ola() {
        return "Olá, mundo!";
    }
}
