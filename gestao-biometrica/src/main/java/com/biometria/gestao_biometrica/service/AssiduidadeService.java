package com.biometria.gestao_biometrica.service;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.repository.RegistoAssiduidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssiduidadeService {

    @Autowired
    private RegistoAssiduidadeRepository registoAssiduidadeRepository;

    // Registar uma entrada ou saída (picar o ponto)
    public RegistoAssiduidade salvar(RegistoAssiduidade registoAssiduidade) {
        return registoAssiduidadeRepository.save(registoAssiduidade);
    }

    // Listar todos os registos
    public List<RegistoAssiduidade> listarTodos() {
        return registoAssiduidadeRepository.findAll();
    }

    // Buscar um registo específico por ID
    public Optional<RegistoAssiduidade> buscarPorId(Long id) {
        return registoAssiduidadeRepository.findById(id);
    }

    // Buscar o histórico de um utilizador específico
    public List<RegistoAssiduidade> buscarPorUtilizador(Long utilizadorId) {
        return registoAssiduidadeRepository.findByUtilizadorId(utilizadorId);
    }
}
