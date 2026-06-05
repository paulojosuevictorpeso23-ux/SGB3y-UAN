package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import com.biometria.gestao_biometrica.dto.LoginRequest;
import com.biometria.gestao_biometrica.dto.AlterarSenhaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilizadores")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilizadorController {

    @Autowired
    private UtilizadorRepository repository;

    @GetMapping
    public List<Utilizador> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public Utilizador criar(@RequestBody Utilizador utilizador) {
        if (utilizador.getPalavraPasse() == null || utilizador.getPalavraPasse().isBlank()) {
            utilizador.setPalavraPasse("123456");
        }
        return repository.save(utilizador);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Utilizador> userOpt = repository.findByUsername(request.getUsername());

        if (userOpt.isPresent() && userOpt.get().getPalavraPasse().equals(request.getPalavraPasse())) {
            Utilizador user = userOpt.get();
            if ("INATIVO".equals(user.getEstado())) {
                return ResponseEntity.status(403).body("Utilizador desativado pelo administrador.");
            }
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Username ou palavra-passe incorretos.");
    }

    @PutMapping("/{id}/reset-senha")
    public ResponseEntity<?> resetSenha(@PathVariable Long id) {
        return repository.findById(id).map(user -> {
            user.setPalavraPasse("123456");
            repository.save(user);
            return ResponseEntity.ok("Palavra-passe resetada para '123456'.");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaRequest request) {
        Optional<Utilizador> userOpt = repository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            Utilizador user = userOpt.get();
            if (user.getPalavraPasse().equals(request.getSenhaAntiga())) {
                user.setPalavraPasse(request.getSenhaNova());
                repository.save(user);
                return ResponseEntity.ok("Palavra-passe alterada com sucesso.");
            }
            return ResponseEntity.badRequest().body("A palavra-passe antiga está incorreta.");
        }
        return ResponseEntity.notFound().build();
    }
}
