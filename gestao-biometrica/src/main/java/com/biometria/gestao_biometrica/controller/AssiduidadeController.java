package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.dto.PontoOnlineDTO;
import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.model.RegistoAssiduidade;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import com.biometria.gestao_biometrica.repository.UtilizadorRepository;
import com.biometria.gestao_biometrica.repository.RegistoAssiduidadeRepository;
import com.biometria.gestao_biometrica.service.AssiduidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/assiduidade")
@CrossOrigin(origins = "*")
public class AssiduidadeController {

    @Autowired
    private AssiduidadeService assiduidadeService;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private RegistoAssiduidadeRepository registoAssiduidadeRepository;

    @Autowired
    private BiometriaRepository biometriaRepository;

    // ==========================================
    // 0. ENDPOINT DO DASHBOARD (ALINHADO COM O FRONTEND)
    // ==========================================
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticasDashboard() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsuarios = utilizadorRepository.count();
        long contagemBanco = utilizadorRepository.countByCargoNome("FUNCIONARIO");
        
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDoDia = LocalDate.now().atTime(LocalTime.MAX);
        long presencasHoje = registoAssiduidadeRepository.countDistinctUtilizadoresByDataHoraBetweenAndTipo(inicioDoDia, fimDoDia, "ENTRADA");
        
        long taxaFrequencia = 0;
        if (contagemBanco > 0) {
            taxaFrequencia = (presencasHoje * 100) / contagemBanco;
            if (taxaFrequencia > 100) taxaFrequencia = 100;
        }

        stats.put("totalUsuarios", totalUsuarios);
        stats.put("presencasHoje", presencasHoje);
        stats.put("funcionariosAtivos", contagemBanco);
        stats.put("taxaFrequencia", taxaFrequencia);

        return ResponseEntity.ok(stats);
    }

    // 1. Rota clássica (Dispositivo físico por parâmetro)
   // 1. Rota clássica (Dispositivo físico por JSON)
    @PostMapping("/bater")
    public ResponseEntity<?> baterPonto(@RequestBody Map<String, Object> body) {
        try {
            // Extrair os dados do JSON de forma segura
            Long utilizadorId = Long.valueOf(body.get("utilizadorId").toString());
            String tipo = body.get("tipo").toString();

            // Chamada ao teu service (que faz o trabalho real no banco)
            RegistoAssiduidade novoRegisto = assiduidadeService.registarPonto(utilizadorId, tipo);
            
            // Retorno de sucesso com o objeto criado
            return ResponseEntity.ok(novoRegisto);
            
        } catch (Exception e) {
            // Retorno de erro caso algo falhe
            return ResponseEntity.badRequest().body("Erro ao registar ponto: " + e.getMessage());
        }
    }

    // 2. Listar todos os pontos
    @GetMapping
    public List<RegistoAssiduidade> listarTodos() {
        return assiduidadeService.listarTodos();
    }

    // 3. Listar por funcionário
    @GetMapping("/utilizador/{utilizadorId}")
    public List<RegistoAssiduidade> listarPorUtilizador(@PathVariable Long utilizadorId) {
        return assiduidadeService.listarPorUtilizador(utilizadorId);
    }

    // 4. Rota Inteligente: Bater ponto por Nome, Username ou ID
    @PostMapping("/bater-online")
    public ResponseEntity<?> baterPontoOnline(@RequestBody Map<String, String> dados) {
        try {
            String tipo = dados.get("tipo"); 
            String identificador = dados.get("identificador");
            
            if (identificador == null || identificador.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: O campo identificador não pode estar vazio.");
            }
            
            identificador = identificador.trim();
            Long utilizadorId = null;

            if (identificador.matches("\\d+")) {
                utilizadorId = Long.parseLong(identificador);
            } else {
                Optional<Utilizador> uOpt = utilizadorRepository.findByUsername(identificador);
                
                if (uOpt.isEmpty()) {
                    uOpt = utilizadorRepository.findByNome(identificador);
                }

                if (uOpt.isPresent()) {
                    utilizadorId = uOpt.get().getId();
                } else {
                    return ResponseEntity.badRequest().body("Erro: Utilizador '" + identificador + "' não foi encontrado no sistema.");
                }
            }

            RegistoAssiduidade novoRegisto = assiduidadeService.registarPonto(utilizadorId, tipo);
            return ResponseEntity.ok(novoRegisto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar ponto online: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. NOVA ROTA: BATER PONTO VIA BIOMETRIA (SIMULADO)
    // ==========================================
    @PostMapping("/bater-biometrico")
    public ResponseEntity<?> baterPontoBiometrico(@RequestBody Map<String, String> dados) {
        try {
            String template = dados.get("templateBiometrico");
            String tipo = dados.get("tipo");

            if (template == null || template.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: O template biométrico não pode estar vazio.");
            }

            Optional<Biometria> bioOpt = biometriaRepository.findByTemplateBiometrico(template.trim());
            
            if (bioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Erro: Impressão digital não reconhecida no sistema.");
            }

            Long utilizadorId = bioOpt.get().getUtilizador().getId();

            RegistoAssiduidade novoRegisto = assiduidadeService.registarPonto(utilizadorId, tipo);
            return ResponseEntity.ok(novoRegisto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar ponto biométrico: " + e.getMessage());
        }
    }

    // ==========================================
    // 6. ENDPOINT DE RELATÓRIO HISTÓRICO GERAL POR DATAS
    // ==========================================
    @GetMapping("/relatorio")
    public ResponseEntity<?> obterRelatorioPorDatas(
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            LocalDateTime dataInicio = (inicio == null || inicio.trim().isEmpty()) ? 
                    LocalDate.now().minusDays(7).atStartOfDay() : LocalDate.parse(inicio).atStartOfDay();

            LocalDateTime dataFim = (fim == null || fim.trim().isEmpty()) ? 
                    LocalDate.now().atTime(LocalTime.MAX) : LocalDate.parse(fim).atTime(LocalTime.MAX);

            if (dataInicio.isAfter(dataFim)) {
                return ResponseEntity.badRequest().body("Erro: A data de início não pode ser posterior à data de fim.");
            }

            List<RegistoAssiduidade> historico = registoAssiduidadeRepository.findByDataHoraBetweenOrderByDataHoraDesc(dataInicio, dataFim);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("totalRegistosEncontrados", historico.size());
            resposta.put("dados", historico);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar relatório: Formato de data inválido. Use AAAA-MM-DD.");
        }
    }

    // ==========================================
    // 7. NOVA ROTA: RELATÓRIO EXCLUSIVO DE FALTAS
    // ==========================================
    @GetMapping("/relatorio/faltas")
    public ResponseEntity<?> obterRelatorioApenasFaltas(
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            LocalDateTime dataInicio = (inicio == null || inicio.trim().isEmpty()) ? 
                    LocalDate.now().minusDays(30).atStartOfDay() : LocalDate.parse(inicio).atStartOfDay();

            LocalDateTime dataFim = (fim == null || fim.trim().isEmpty()) ? 
                    LocalDate.now().atTime(LocalTime.MAX) : LocalDate.parse(fim).atTime(LocalTime.MAX);

            List<RegistoAssiduidade> todosRegistos = registoAssiduidadeRepository.findByDataHoraBetweenOrderByDataHoraDesc(dataInicio, dataFim);
            
            // Filtrar na Stream apenas o que for do tipo "FALTA"
            List<RegistoAssiduidade> apenasFaltas = todosRegistos.stream()
                    .filter(r -> "FALTA".equalsIgnoreCase(r.getTipo()))
                    .toList();

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("totalFaltasEncontradas", apenasFaltas.size());
            resposta.put("periodo", dataInicio.toLocalDate() + " ate " + dataFim.toLocalDate());
            resposta.put("dados", apenasFaltas);

            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar relatório de faltas: " + e.getMessage());
        }
    }

    // ==========================================
    // 8. NOVA ROTA: RESUMO ESTATÍSTICO INDIVIDUAL (MENSAL/PERÍODO)
    // ==========================================
    @GetMapping("/relatorio/utilizador/{utilizadorId}")
    public ResponseEntity<?> obterResumoIndividual(
            @PathVariable Long utilizadorId,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            Optional<Utilizador> uOpt = utilizadorRepository.findById(utilizadorId);
            if (uOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Erro: Utilizador não encontrado.");
            }

            LocalDateTime dataInicio = (inicio == null || inicio.trim().isEmpty()) ? 
                    LocalDate.now().minusDays(30).atStartOfDay() : LocalDate.parse(inicio).atStartOfDay();

            LocalDateTime dataFim = (fim == null || fim.trim().isEmpty()) ? 
                    LocalDate.now().atTime(LocalTime.MAX) : LocalDate.parse(fim).atTime(LocalTime.MAX);

            // Obter todo o histórico do utilizador específico
            List<RegistoAssiduidade> historicoUser = registoAssiduidadeRepository.findAll().stream()
                    .filter(p -> p.getUtilizador().getId().equals(utilizadorId)
                            && p.getDataHora().isAfter(dataInicio)
                            && p.getDataHora().isBefore(dataFim))
                    .toList();

            // Computar métricas locais
            long totalPresencas = historicoUser.stream().filter(p -> "ENTRADA".equalsIgnoreCase(p.getTipo())).count();
            long totalFaltas = historicoUser.stream().filter(p -> "FALTA".equalsIgnoreCase(p.getTipo())).count();
            long totalAtrasos = historicoUser.stream().filter(p -> "ATRASO".equalsIgnoreCase(p.getEstadoPonto())).count();

            Map<String, Object> resumo = new HashMap<>();
            resumo.put("funcionarioId", utilizadorId);
            resumo.put("nome", uOpt.get().getNome());
            resumo.put("totalPresencas", totalPresencas);
            resumo.put("totalFaltas", totalFaltas);
            resumo.put("totalAtrasos", totalAtrasos);
            resumo.put("historicoCompletoNoPeriodo", historicoUser);

            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar resumo individual: " + e.getMessage());
        }
    }
    
    
    // ==========================================
    // 9. NOVA ROTA: BATER PONTO MANUAL (ADMIN)
    // ==========================================
    @PostMapping("/manual/{utilizadorId}")
    public ResponseEntity<?> baterPontoManualAdmin(@PathVariable Long utilizadorId, @RequestBody Map<String, String> body) {
        try {
            // O tipo (ex: "ENTRADA" ou "SAIDA") vem do corpo da requisição ou podes fixar como "MANUAL_ADMIN"
            String tipo = body.getOrDefault("tipo", "ENTRADA");
            
            // Reutiliza o método que já tens no teu service
            RegistoAssiduidade novoRegisto = assiduidadeService.registarPonto(utilizadorId, tipo);
            
            // Opcional: Atualizar o estado se necessário
            novoRegisto.setEstadoPonto("MANUAL_ADMIN"); 
            registoAssiduidadeRepository.save(novoRegisto);

            return ResponseEntity.ok(novoRegisto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registar ponto manual: " + e.getMessage());
        }
    }
}

