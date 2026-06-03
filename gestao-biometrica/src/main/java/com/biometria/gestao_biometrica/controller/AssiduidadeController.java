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

    // 1. CREATE -> POST http://localhost:8080/api/assiduidade
    @PostMapping
    public ResponseEntity<RegistoAssiduidade> registarPonto(@RequestBody RegistoAssiduidade registoAssiduidade) {
        RegistoAssiduidade novoRegisto = assiduidadeService.salvar(registoAssiduidade);
        return ResponseEntity.ok(novoRegisto);
    }

    // 2. READ -> GET http://localhost:8080/api/assiduidade
    @GetMapping
    public List<RegistoAssiduidade> listarTodos() {
        return assiduidadeService.listarTodos();
    }

    // 2. READ -> GET http://localhost:8080/api/assiduidade/utilizador/{utilizadorId}
    @GetMapping("/utilizador/{utilizadorId}")
    public List<RegistoAssiduidade> buscarPorUtilizador(@PathVariable Long utilizadorId) {
        return assiduidadeService.buscarPorUtilizador(utilizadorId);
    }
}
