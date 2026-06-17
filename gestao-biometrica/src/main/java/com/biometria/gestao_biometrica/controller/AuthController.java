package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String username = credenciais.get("username");
        String palavraPasse = credenciais.get("palavraPasse");

        if (username == null || palavraPasse == null) {
            return ResponseEntity.badRequest().body("Erro: Username e palavra-passe são obrigatórios.");
        }

        // 1. Procurar o utilizador pelo username
        Optional<Utilizador> uOpt = utilizadorRepository.findByUsername(username.trim());

        if (uOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Erro: Utilizador não encontrado.");
        }

        Utilizador utilizador = uOpt.get();

        // 2. Verificar se está INATIVO
        if ("INATIVO".equalsIgnoreCase(utilizador.getEstado())) {
            return ResponseEntity.status(403).body("Erro: Este utilizador está desativado no sistema.");
        }

        // 3. Validar a palavra-passe (Simples para o teu ambiente de desenvolvimento)
        if (!utilizador.getPalavraPasse().equals(palavraPasse)) {
            return ResponseEntity.status(401).body("Erro: Palavra-passe incorreta.");
        }

        // 4. Se tudo bater certo, envia os dados do utilizador logado para o Frontend
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Login efetuado com sucesso!");
        resposta.put("id", utilizador.getId());
        resposta.put("nome", utilizador.getNome());
        resposta.put("username", utilizador.getUsername());
        resposta.put("cargo", utilizador.getCargo().getNome()); // ADMIN ou FUNCIONARIO

        return ResponseEntity.ok(resposta);
    }
}

