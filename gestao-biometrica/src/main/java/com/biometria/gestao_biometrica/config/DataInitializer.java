package com.biometria.gestao_biometrica.config;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UtilizadorRepository repository) {
        return args -> {
            // Se a tabela de utilizadores estiver vazia, cria o primeiro Admin
            if (repository.count() == 0) {
                Utilizador admin = new Utilizador();
                admin.setNome("Administrador do Sistema");
                admin.setUsername("admin");
                admin.setPalavraPasse("admin123"); // Senha padrão para o teu MVP
                admin.setCargo("ADMIN");
                admin.setEstado("ATIVO");

                repository.save(admin);
                System.out.println("\n=================================================");
                System.out.println(" 🎉 UTILIZADOR ADMIN PADRÃO CRIADO COM SUCESSO!");
                System.out.println(" 👉 Username: admin");
                System.out.println(" 👉 Palavra-passe: admin123");
                System.out.println("=================================================\n");
            }
        };
    }
}
