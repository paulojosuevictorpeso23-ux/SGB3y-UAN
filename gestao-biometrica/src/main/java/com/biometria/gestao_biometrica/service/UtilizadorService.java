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
    private UtilizadorRepository repository;

    // Listar todos os funcionários
    public List<Utilizador> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID - Retorna Utilizador diretamente (corrige os erros das linhas 31 e 72)
    public Utilizador buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizador com ID " + id + " não encontrado!"));
    }

    // Buscar por Username - Retorna Optional (necessário para a lógica de login)
    public Optional<Utilizador> buscarPorUsername(String username) {
        return repository.findByUsername(username);
    }

    // Criar novo funcionário (corrige o erro da linha 37)
    public Utilizador criar(Utilizador utilizador) {
        if (utilizador.getPalavraPasse() == null || utilizador.getPalavraPasse().isBlank()) {
            utilizador.setPalavraPasse("123456");
        }
        return repository.save(utilizador);
    }

    // Atualizar dados (corrige o erro da linha 43)
    public Utilizador atualizar(Long id, Utilizador dadosAtualizados) {
        Utilizador existente = buscarPorId(id);

        existente.setNome(dadosAtualizados.getNome());
        existente.setUsername(dadosAtualizados.getUsername());
        existente.setEmail(dadosAtualizados.getEmail());
        existente.setContacto(dadosAtualizados.getContacto());
        existente.setEstado(dadosAtualizados.getEstado());
        
        if (dadosAtualizados.getCargo() != null) {
            existente.setCargo(dadosAtualizados.getCargo());
        }
        if (dadosAtualizados.getHorario() != null) {
            existente.setHorario(dadosAtualizados.getHorario());
        }

        return repository.save(existente);
    }

    // Eliminar funcionário
    public void eliminar(Long id) {
        Utilizador utilizador = buscarPorId(id);
        repository.delete(utilizador);
    }

    // Salvar alterações diretas (como senhas)
    public void salvar(Utilizador utilizador) {
        repository.save(utilizador);
    }
}
