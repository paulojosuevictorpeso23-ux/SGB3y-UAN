package com.biometria.gestao_biometrica.service;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilizadorService {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    // Lógica para Criar ou Atualizar (Save)
    public Utilizador salvar(Utilizador utilizador) {
        if (utilizador.getPalavraPasse() == null || utilizador.getPalavraPasse().isBlank()) {
            utilizador.setPalavraPasse("123456");
        }
        return utilizadorRepository.save(utilizador);
    }

    // Lógica para Listar todos (Read)
    public List<Utilizador> listarTodos() {
        return utilizadorRepository.findAll();
    }

    // Lógica para Procurar por ID (Read)
    public Optional<Utilizador> buscarPorId(Long id) {
        return utilizadorRepository.findById(id);
    }

    // Lógica para Procurar pelo username (Read) - CORRIGIDO
    public Optional<Utilizador> buscarPorUsername(String username) {
        return utilizadorRepository.findByUsername(username);
    }

    // Lógica para Apagar (Delete)
    public void eliminar(Long id) {
        utilizadorRepository.deleteById(id);
    }
}
