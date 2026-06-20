package com.biometria.gestao_biometrica.service;

import com.machinezoo.sourceafis.*;
import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.List;

@Service
public class BiometriaService {

    @Autowired
    private BiometriaRepository biometriaRepository;

    public BiometriaService() {} 
    
    // Construtor essencial para o teste unitário funcionar
    public BiometriaService(BiometriaRepository repo) { 
        this.biometriaRepository = repo; 
    }
    
   public String identificarUtilizador(String templateBase64) {
    try {
        byte[] templateBytes = Base64.getDecoder().decode(templateBase64);
        FingerprintTemplate probe = new FingerprintTemplate(templateBytes);

        List<Biometria> baseDeDados = biometriaRepository.findAll();
        for (Biometria b : baseDeDados) {
            byte[] candidateBytes = Base64.getDecoder().decode(b.getTemplateBiometrico());
            FingerprintTemplate candidate = new FingerprintTemplate(candidateBytes);
            
            double score = new FingerprintMatcher(probe).match(candidate);
            if (score >= 40) {
                return b.getUtilizador().getNome();
            }
        }
        return "Nenhum utilizador encontrado";
    } catch (Exception e) {
        return "Erro: Template biométrico inválido.";
    }
}
}


