package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.model.Cargo;
import com.biometria.gestao_biometrica.model.Horario;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import com.biometria.gestao_biometrica.repository.CargoRepository;
import com.biometria.gestao_biometrica.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilizadores")
@CrossOrigin(origins = "*")
public class UtilizadorController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    // 1. LISTAR TODOS OS UTILIZADORES
    @GetMapping
    public List<Utilizador> listarTodos() {
        return utilizadorRepository.findAll();
    }

    // 2. BUSCAR UTILIZADOR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Utilizador> uOpt = utilizadorRepository.findById(id);
        if (uOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erro: Utilizador com ID " + id + " não encontrado.");
        }
        return ResponseEntity.ok(uOpt.get());
    }

    // 3. CADASTRAR NOVO UTILIZADOR
    @PostMapping
    public ResponseEntity<?> cadastrarUtilizador(@RequestBody Map<String, Object> dados) {
        try {
            String username = (String) dados.get("username");
            String email = (String) dados.get("email");

            if (utilizadorRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body("Erro: O nome de utilizador '" + username + "' já está em uso.");
            }

            if (email != null && !email.trim().isEmpty()) {
                Optional<Utilizador> uOpt = utilizadorRepository.findAll().stream()
                        .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                        .findFirst();
                if (uOpt.isPresent()) {
                    return ResponseEntity.badRequest().body("Erro: O e-mail '" + email + "' já está registado.");
                }
            }

            Utilizador novo = new Utilizador();
            novo.setNome((String) dados.get("nome"));
            novo.setUsername(username);
            novo.setPalavraPasse((String) dados.get("palavraPasse"));
            novo.setEmail(email);
            novo.setContacto((String) dados.get("contacto"));
            novo.setEstado("ATIVO");
            novo.setDataCriacao(LocalDateTime.now());
            novo.setDataAtualizacao(LocalDateTime.now());

            Long cargoId = Long.valueOf(dados.get("cargoId").toString());
            Optional<Cargo> cargoOpt = cargoRepository.findById(cargoId);
            if (cargoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Cargo com ID " + cargoId + " não existe.");
            }
            novo.setCargo(cargoOpt.get());

            Long horarioId = Long.valueOf(dados.get("horarioId").toString());
            Optional<Horario> horarioOpt = horarioRepository.findById(horarioId);
            if (horarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Horário com ID " + horarioId + " não existe.");
            }
            novo.setHorario(horarioOpt.get());

            Utilizador salvo = utilizadorRepository.save(novo);
            return ResponseEntity.ok(salvo);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar cadastro: " + e.getMessage());
        }
    }

    // 4. ATUALIZAR UTILIZADOR EXISTENTE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUtilizador(@PathVariable Long id, @RequestBody Map<String, Object> dados) {
        try {
            Optional<Utilizador> uOpt = utilizadorRepository.findById(id);
            if (uOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Erro: Utilizador com ID " + id + " não encontrado.");
            }

            Utilizador utilizador = uOpt.get();
            String novoUsername = (String) dados.get("username");
            String novoEmail = (String) dados.get("email");

            // Validar se o novo username já pertence a OUTRA pessoa
            Optional<Utilizador> userConflicto = utilizadorRepository.findByUsername(novoUsername);
            if (userConflicto.isPresent() && !userConflicto.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Erro: O username '" + novoUsername + "' já está a ser usado por outro utilizador.");
            }

            // Atualizar os campos básicos
            utilizador.setNome((String) dados.get("nome"));
            utilizador.setUsername(novoUsername);
            utilizador.setEmail(novoEmail);
            utilizador.setContacto((String) dados.get("contacto"));
            
            if (dados.get("palavraPasse") != null && !dados.get("palavraPasse").toString().isEmpty()) {
                utilizador.setPalavraPasse((String) dados.get("palavraPasse"));
            }
            
            if (dados.get("estado") != null) {
                utilizador.setEstado((String) dados.get("estado"));
            }

            // Atualizar Cargo se enviado
            if (dados.get("cargoId") != null) {
                Long cargoId = Long.valueOf(dados.get("cargoId").toString());
                Optional<Cargo> cargoOpt = cargoRepository.findById(cargoId);
                if (cargoOpt.isPresent()) utilizador.setCargo(cargoOpt.get());
            }

            // Atualizar Horário se enviado
            if (dados.get("horarioId") != null) {
                Long horarioId = Long.valueOf(dados.get("horarioId").toString());
                Optional<Horario> horarioOpt = horarioRepository.findById(horarioId);
                if (horarioOpt.isPresent()) utilizador.setHorario(horarioOpt.get());
            }

            utilizador.setDataAtualizacao(LocalDateTime.now());
            Utilizador atualizado = utilizadorRepository.save(utilizador);
            
            return ResponseEntity.ok(atualizado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }

    // 5. DESATIVAR UTILIZADOR (SOFT DELETE VIA ENUM/STRING ESTADO)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desativarUtilizador(@PathVariable Long id) {
        Optional<Utilizador> uOpt = utilizadorRepository.findById(id);
        if (uOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Erro: Utilizador com ID " + id + " não encontrado.");
        }

        Utilizador utilizador = uOpt.get();
        utilizador.setEstado("INATIVO"); // <--- Soft delete seguro para manter histórico de pontos
        utilizador.setDataAtualizacao(LocalDateTime.now());
        
        utilizadorRepository.save(utilizador);
        return ResponseEntity.ok("Utilizador '" + utilizador.getNome() + "' foi desativado com sucesso.");
    }
}

