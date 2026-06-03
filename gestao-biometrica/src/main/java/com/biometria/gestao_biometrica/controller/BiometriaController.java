package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.service.BiometriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biometrias")
@CrossOrigin(origins = "*")
public class BiometriaController {

    @Autowired
    private BiometriaService biometriaService;

    @PostMapping
    public ResponseEntity<Biometria> criar(@RequestBody Biometria biometria) {
        Biometria novaBiometria = biometriaService.salvar(biometria);
        return ResponseEntity.ok(novaBiometria);
    }

    @GetMapping
    public List<Biometria> listar() {
        return biometriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biometria> buscarPorId(@PathVariable Long id) {
        return biometriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utilizador/{utilizadorId}")
    public List<Biometria> buscarPorUtilizador(@PathVariable Long utilizadorId) {
        return biometriaService.buscarPorUtilizador(utilizadorId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biometria> atualizar(@PathVariable Long id, @RequestBody Biometria dadosAtualizados) {
        return biometriaService.buscarPorId(id).map(biometria -> {
            biometria.setSensorTemplateId(dadosAtualizados.getSensorTemplateId());
            biometria.setUtilizador(dadosAtualizados.getUtilizador());
            Biometria atualizada = biometriaService.salvar(biometria);
            return ResponseEntity.ok(atualizada);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (biometriaService.buscarPorId(id).isPresent()) {
            biometriaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
