package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilizadores")
@CrossOrigin(origins = "*") // Permite que o Angular se ligue à API sem bloqueios de segurança
public class UtilizadorController {

    @Autowired
    private UtilizadorService utilizadorService;

    // 1. CREATE (Criar Utilizador) -> POST http://localhost:8080/api/utilizadores
    @PostMapping
    public ResponseEntity<Utilizador> criar(@RequestBody Utilizador utilizador) {
        Utilizador novoUtilizador = utilizadorService.salvar(utilizador);
        return ResponseEntity.ok(novoUtilizador);
    }

    // 2. READ (Listar Todos) -> GET http://localhost:8080/api/utilizadores
    @GetMapping
    public List<Utilizador> listar() {
        return utilizadorService.listarTodos();
    }

    // 2. READ (Buscar por ID) -> GET http://localhost:8080/api/utilizadores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Utilizador> buscarPorId(@PathVariable Long id) {
        return utilizadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. UPDATE (Atualizar Utilizador) -> PUT http://localhost:8080/api/utilizadores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Utilizador> atualizar(@PathVariable Long id, @RequestBody Utilizador dadosAtualizados) {
        return utilizadorService.buscarPorId(id).map(utilizador -> {
            utilizador.setNome(dadosAtualizados.getNome());
            utilizador.setIdentificacaoEscolar(dadosAtualizados.getIdentificacaoEscolar());
            utilizador.setCargo(dadosAtualizados.getCargo());
            utilizador.setEstado(dadosAtualizados.getEstado());
            Utilizador atualizado = utilizadorService.salvar(utilizador);
            return ResponseEntity.ok(atualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. DELETE (Eliminar Utilizador) -> DELETE http://localhost:8080/api/utilizadores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (utilizadorService.buscarPorId(id).isPresent()) {
            utilizadorService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
