package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.service.AssiduidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assiduidade")
@CrossOrigin(origins = "*")
public class AssiduidadeController {

    @Autowired
    private AssiduidadeService assiduidadeService;

    // Rota para registar uma picagem de ponto
    // Exemplo de uso: POST http://localhost:8080/api/assiduidade/bater?utilizadorId=1&tipo=ENTRADA
    @PostMapping("/bater")
    public ResponseEntity<RegistoAssiduidade> baterPonto(@RequestParam Long utilizadorId, @RequestParam String tipo) {
        try {
            RegistoAssiduidade novoRegisto = assiduidadeService.registarPonto(utilizadorId, tipo);
            return ResponseEntity.ok(novoRegisto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Rota para o Admin listar absolutamente todos os pontos registados no sistema
    @GetMapping
    public List<RegistoAssiduidade> listarTodos() {
        return assiduidadeService.listarTodos();
    }

    // Rota para listar o histórico de assiduidade de um funcionário específico
    @GetMapping("/utilizador/{utilizadorId}")
    public List<RegistoAssiduidade> listarPorUtilizador(@PathVariable Long utilizadorId) {
        return assiduidadeService.listarPorUtilizador(utilizadorId);
    }
}
