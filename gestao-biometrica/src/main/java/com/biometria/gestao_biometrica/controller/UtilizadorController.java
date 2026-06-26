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
import org.springframework.transaction.annotation.Transactional;

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

    // 1. LISTAR 
@GetMapping
public List<Utilizador> listarTodos() {
    return utilizadorRepository.findAll(); // Busca tudo, sem filtros
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
            // -- AQUI É ONDE COMEÇA A ALTERAÇÃO --
            String username = (dados.get("username") != null) ? dados.get("username").toString() : "";
            String email = (dados.get("email") != null) ? dados.get("email").toString() : "";

            if (utilizadorRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body("Erro: O nome de utilizador '" + username + "' já está em uso.");
            }

            Utilizador novo = new Utilizador();
            novo.setNome(dados.get("nome") != null ? dados.get("nome").toString() : "");
            novo.setUsername(username);
            novo.setPalavraPasse(dados.get("palavraPasse") != null ? dados.get("palavraPasse").toString() : "");
            novo.setEmail(email);
            novo.setContacto(dados.get("contacto") != null ? dados.get("contacto").toString() : "");
            novo.setEstado("ATIVO");
            novo.setDataCriacao(LocalDateTime.now());
            novo.setDataAtualizacao(LocalDateTime.now());

            // Conversão segura dos IDs
            Object cId = dados.get("cargoId");
            Object hId = dados.get("horarioId");

            if (cId == null || hId == null) {
                return ResponseEntity.badRequest().body("Erro: Cargo ou Horário não selecionados.");
            }

            Long cargoId = Long.valueOf(String.valueOf(cId));
            Long horarioId = Long.valueOf(String.valueOf(hId));

            Optional<Cargo> cargoOpt = cargoRepository.findById(cargoId);
            if (cargoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Cargo com ID " + cargoId + " não existe.");
            }
            novo.setCargo(cargoOpt.get());

            Optional<Horario> horarioOpt = horarioRepository.findById(horarioId);
            if (horarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Horário com ID " + horarioId + " não existe.");
            }
            novo.setHorario(horarioOpt.get());

            Utilizador salvo = utilizadorRepository.save(novo);
            return ResponseEntity.ok(salvo);
            // -- AQUI TERMINA A ALTERAÇÃO --

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

    // 5. DESATIVAR UTILIZADOR
@DeleteMapping("/{id}")
@Transactional // <--- ISSO É O QUE FALTA PARA GRAVAR
public ResponseEntity<Void> desativarUtilizador(@PathVariable Long id) {
    Optional<Utilizador> uOpt = utilizadorRepository.findById(id);
    if (uOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Utilizador utilizador = uOpt.get();
    utilizador.setEstado("INATIVO"); // Certifique-se de que o campo na BD é exatamente "INATIVO"
    utilizador.setDataAtualizacao(LocalDateTime.now());
    
    utilizadorRepository.save(utilizador); // Agora será gravado com sucesso
    
    return ResponseEntity.noContent().build();
}
}

