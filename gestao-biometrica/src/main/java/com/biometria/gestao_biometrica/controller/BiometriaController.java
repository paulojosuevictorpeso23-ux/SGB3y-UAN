package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import com.biometria.gestao_biometrica.service.BiometriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/biometria")
@CrossOrigin(origins = "*")
public class BiometriaController {

    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private BiometriaService biometriaService;

    // ==========================================
    // REGISTAR NOVA BIOMETRIA
    // ==========================================
    @PostMapping("/registo")
    public ResponseEntity<?> registarBiometria(@RequestBody Map<String, Object> dados) {
        try {
            if (!dados.containsKey("utilizadorId") || !dados.containsKey("templateBiometrico") || !dados.containsKey("dedoRegistado")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios em falta.");
            }

            Long utilizadorId = Long.valueOf(dados.get("utilizadorId").toString());
            String template = dados.get("templateBiometrico").toString().trim();
            String dedo = dados.get("dedoRegistado").toString().trim();

            Optional<Utilizador> uOpt = utilizadorRepository.findById(utilizadorId);
            if (uOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Utilizador não encontrado.");
            }

            Biometria novaBiometria = new Biometria();
            novaBiometria.setUtilizador(uOpt.get());
            novaBiometria.setTemplateBiometrico(template);
            novaBiometria.setDedoRegistado(dedo);

            Biometria biometriaSalva = biometriaRepository.save(novaBiometria);

            return ResponseEntity.ok(Map.of(
                "mensagem", "Impressão digital registada com sucesso!",
                "idBiometria", biometriaSalva.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registar: " + e.getMessage());
        }
    }

    // ==========================================
    // IDENTIFICAR UTILIZADOR (Motor de Match)
    // ==========================================
    @PostMapping("/identificar")
    public ResponseEntity<?> identificar(@RequestBody Map<String, String> dados) {
        String template = dados.get("templateBiometrico");
        
        if (template == null || template.isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: Template em falta.");
        }
        
        // Chama o serviço que validámos com os testes unitários
        String nomeUtilizador = biometriaService.identificarUtilizador(template);
        
        return ResponseEntity.ok(Map.of("utilizador", nomeUtilizador));
    }
}
