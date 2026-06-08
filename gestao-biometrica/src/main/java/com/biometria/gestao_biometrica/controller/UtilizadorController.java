package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.service.UtilizadorService;
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
    private UtilizadorService utilizadorService;

    @GetMapping
    public List<Utilizador> listarTodos() {
        return utilizadorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilizador> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(utilizadorService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Utilizador criar(@RequestBody Utilizador utilizador) {
        return utilizadorService.criar(utilizador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilizador> atualizar(@PathVariable Long id, @RequestBody Utilizador utilizador) {
        try {
            return ResponseEntity.ok(utilizadorService.atualizar(id, utilizador));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            utilizadorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Utilizador> userOpt = utilizadorService.buscarPorUsername(request.getUsername());

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
        try {
            Utilizador user = utilizadorService.buscarPorId(id);
            user.setPalavraPasse("123456");
            utilizadorService.salvar(user);
            return ResponseEntity.ok("Palavra-passe resetada para '123456'.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaRequest request) {
        Optional<Utilizador> userOpt = utilizadorService.buscarPorUsername(request.getUsername());

        if (userOpt.isPresent()) {
            Utilizador user = userOpt.get();
            if (user.getPalavraPasse().equals(request.getSenhaAntiga())) {
                user.setPalavraPasse(request.getSenhaNova());
                utilizadorService.salvar(user);
                return ResponseEntity.ok("Palavra-passe alterada com sucesso.");
            }
            return ResponseEntity.badRequest().body("A palavra-passe antiga está incorreta.");
        }
        return ResponseEntity.notFound().build();
    }
}
