package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
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

    // ==========================================
    // REGISTAR NOVA BIOMETRIA (SIMULADO)
    // ==========================================
    @PostMapping("/registo")
    public ResponseEntity<?> registarBiometria(@RequestBody Map<String, Object> dados) {
        try {
            // 1. Extrair e validar os dados do JSON
            if (!dados.containsKey("utilizadorId") || !dados.containsKey("templateBiometrico") || !dados.containsKey("dedoRegistado")) {
                return ResponseEntity.badRequest().body("Erro: Campos obrigatórios em falta (utilizadorId, templateBiometrico, dedoRegistado).");
            }

            Long utilizadorId = Long.valueOf(dados.get("utilizadorId").toString());
            String template = dados.get("templateBiometrico").toString().trim();
            String dedo = dados.get("dedoRegistado").toString().trim();

            if (template.isEmpty() || dedo.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: O template biométrico e o dedo registado não podem estar vazios.");
            }

            // 2. Verificar se o utilizador realmente existe no banco de dados
            Optional<Utilizador> uOpt = utilizadorRepository.findById(utilizadorId);
            if (uOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Utilizador com ID " + utilizadorId + " não foi encontrado.");
            }

            // 3. Criar a nova entidade Biometria e preencher os dados
            Biometria novaBiometria = new Biometria();
            novaBiometria.setUtilizador(uOpt.get());
            novaBiometria.setTemplateBiometrico(template);
            novaBiometria.setDedoRegistado(dedo);

            // O @PrePersist na tua entidade vai tratar de colocar a dataHora atual automaticamente!
            
            // 4. Salvar no PostgreSQL de forma segura
            Biometria biometriaSalva = biometriaRepository.save(novaBiometria);

            return ResponseEntity.ok(Map.of(
                "mensagem", "Impressão digital registada com sucesso!",
                "idBiometria", biometriaSalva.getId(),
                "utilizador", biometriaSalva.getUtilizador().getNome(),
                "dedo", biometriaSalva.getDedoRegistado()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registar biometria: " + e.getMessage());
        }
    }
}

