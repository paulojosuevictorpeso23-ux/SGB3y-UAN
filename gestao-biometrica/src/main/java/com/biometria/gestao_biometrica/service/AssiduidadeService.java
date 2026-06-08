package com.biometria.gestao_biometrica.service;

import com.biometria.gestao_biometrica.model.*;
import com.biometria.gestao_biometrica.repository.RegistoAssiduidadeRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AssiduidadeService {

    @Autowired
    private RegistoAssiduidadeRepository assiduidadeRepository;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    // Alterado de 'baterPonto' para 'registarPonto' para casar com o AssiduidadeController
    public RegistoAssiduidade registarPonto(Long utilizadorId, String tipo) {
        Utilizador utilizador = utilizadorRepository.findById(utilizadorId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado!"));

        RegistoAssiduidade registo = new RegistoAssiduidade();
        registo.setUtilizador(utilizador);
        registo.setTipo(tipo.toUpperCase());
        
        // Capturamos o momento atual para fazer o cálculo matemático
        LocalDateTime agora = LocalDateTime.now();
        registo.setDataHora(agora);

        LocalTime horaAtual = agora.toLocalTime();
        Horario horario = utilizador.getHorario();

        // LÓGICA DE VALIDAÇÃO DE HORÁRIO (OPÇÃO 1)
        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            // Hora Limite = 08:00 + 15 minutos de tolerância = 08:15
            LocalTime horaLimite = horario.getHoraEntrada().plusMinutes(horario.getToleranciaMinutos());
            
            if (horaAtual.isAfter(horaLimite)) {
                registo.setEstadoPonto("ATRASADO");
            } else {
                registo.setEstadoPonto("A_TEMPO");
            }
        } else if ("SAIDA".equalsIgnoreCase(tipo)) {
            // Se tentar sair antes das 16:00
            if (horaAtual.isBefore(horario.getHoraSaida())) {
                registo.setEstadoPonto("SAIDA_ANTECIPADA");
            } else {
                registo.setEstadoPonto("SAIDA_NORMAL");
            }
        }

        return assiduidadeRepository.save(registo);
    }

    public List<RegistoAssiduidade> listarTodos() {
        return assiduidadeRepository.findAll();
    }

    public List<RegistoAssiduidade> listarPorUtilizador(Long utilizadorId) {
        Utilizador utilizador = utilizadorRepository.findById(utilizadorId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado!"));
        return assiduidadeRepository.findByUtilizadorOrderByDataHoraDesc(utilizador);
    }
}
