package com.biometria.gestao_biometrica.config;

import com.biometria.gestao_biometrica.model.Cargo;
import com.biometria.gestao_biometrica.model.Horario;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.CargoRepository;
import com.biometria.gestao_biometrica.repository.HorarioRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UtilizadorRepository userRepo, 
                                   CargoRepository cargoRepo, 
                                   HorarioRepository horarioRepo) {
        return args -> {
            
            // 1. Garante que o Cargo ADMIN existe na base de dados (receberá o ID 1)
            Cargo cargoAdmin = cargoRepo.findByNome("ADMIN")
                    .orElseGet(() -> cargoRepo.save(new Cargo(null, "ADMIN")));

            // 2. Garante que o Cargo FUNCIONARIO existe na base de dados (receberá o ID 2)
            // Isto previne o erro de violação de chave estrangeira (foreign key constraint) ao registar pelo Angular
            Cargo cargoFuncionario = cargoRepo.findByNome("FUNCIONARIO")
                    .orElseGet(() -> cargoRepo.save(new Cargo(null, "FUNCIONARIO")));

            // 3. Garante que existe pelo menos um Horário padrão na base de dados
            Horario horarioPadrao = horarioRepo.findAll().stream().findFirst()
                    .orElseGet(() -> horarioRepo.save(new Horario(null, "Turno Geral", LocalTime.of(8, 0), LocalTime.of(16, 0), 15)));

            // 4. Se a tabela de utilizadores estiver vazia, cria o primeiro Admin do Sistema
            if (userRepo.count() == 0) {
                Utilizador admin = new Utilizador();
                admin.setNome("Administrador do Sistema");
                admin.setUsername("admin");
                admin.setPalavraPasse("admin123"); // Futuramente aplicaremos encriptação com BCrypt aqui
                admin.setEmail("admin@uan.ao"); 
                admin.setContacto("+244900000000");
                admin.setCargo(cargoAdmin); // Vincula o objeto Cargo ADMIN real (ID 1)
                admin.setHorario(horarioPadrao); // Vincula o objeto Horario real
                admin.setEstado("ATIVO");

                userRepo.save(admin);
                System.out.println("\n=================================================");
                System.out.println(" 🎉 AMBIENTE SGB3y-UAN INICIALIZADO COM SUCESSO!");
                System.out.println(" 👉 Cargo ADMIN (ID 1) e FUNCIONARIO (ID 2) prontos.");
                System.out.println(" 👉 Utilizador Administrativo padrão criado.");
                System.out.println(" 👉 Username: admin | Palavra-passe: admin123");
                System.out.println("=================================================\n");
            }
        };
    }
}

