package com.biometria.gestao_biometrica.scheduler;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.RegistoAssiduidadeRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class AssiduidadeScheduler {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private RegistoAssiduidadeRepository registoAssiduidadeRepository;

    // Roda TODOS OS DIAS à meia-noite (00:00:00) automaticamente
    @Scheduled(cron = "0 0 0 * * ?")
    public void verificarFaltasDoDiaAnterior() {
        System.out.println("====== [SCHEDULER] A iniciar verificação de faltas do dia anterior ======");
        
        // 1. Pegar a data do dia que acabou de terminar
        LocalDate ontem = LocalDate.now().minusDays(1);
        LocalDateTime inicioOntem = ontem.atStartOfDay();
        LocalDateTime fimOntem = ontem.atTime(LocalTime.MAX);

        // 2. Buscar todos os utilizadores ativos com cargo "FUNCIONARIO"
        List<Utilizador> funcionarios = utilizadorRepository.findAll().stream()
                .filter(u -> "ATIVO".equalsIgnoreCase(u.getEstado()) && 
                             "FUNCIONARIO".equalsIgnoreCase(u.getCargo().getNome()))
                .toList();

        for (Utilizador funcionario : funcionarios) {
            // 3. Verificar se o funcionário bateu alguma ENTRADA ontem
            long registosOntem = registoAssiduidadeRepository
                    .countDistinctUtilizadoresByDataHoraBetweenAndTipo(inicioOntem, fimOntem, "ENTRADA");

            // Nota: Se o teu Repository exigir o ID do utilizador na busca, podes filtrar os pontos dele.
            // Para garantir precisão, vamos ver se ELE especificamente tem ponto:
            boolean temPonto = registoAssiduidadeRepository.findAll().stream()
                    .anyMatch(p -> p.getUtilizador().getId().equals(funcionario.getId()) 
                              && p.getDataHora().isAfter(inicioOntem) 
                              && p.getDataHora().isBefore(fimOntem)
                              && "ENTRADA".equalsIgnoreCase(p.getTipo()));

            // 4. Se não tiver nenhum ponto de entrada, geramos a FALTA automática
            if (!temPonto) {
                RegistoAssiduidade falta = new RegistoAssiduidade();
                falta.setUtilizador(funcionario);
                falta.setDataHora(ontem.atTime(23, 59, 59)); // Grava no último segundo do dia da falta
                falta.setTipo("FALTA");
                falta.setEstadoPonto("FALTA_INJUSTIFICADA");
                
                registoAssiduidadeRepository.save(falta);
                System.out.println("Falta automática registada para: " + funcionario.getNome());
            }
        }
        System.out.println("====== [SCHEDULER] Verificação de faltas concluída com sucesso ======");
    }
}

