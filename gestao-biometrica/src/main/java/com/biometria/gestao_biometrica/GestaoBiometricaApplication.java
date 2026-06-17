package com.biometria.gestao_biometrica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- IMPORTANTE

@SpringBootApplication
@EnableScheduling // <-- ATIVA O AGENDADOR AUTOMÁTICO
public class GestaoBiometricaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoBiometricaApplication.class, args);
    }
}

