package com.biometria.gestao_biometrica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // IMPORTA ISTO
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing // <--- ADICIONA ISTO AQUI
public class GestaoBiometricaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoBiometricaApplication.class, args);
    }
}

