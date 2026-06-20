package com.biometria.gestao_biometrica;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import java.util.Random;

public class BiometriaMatcherTest {

    @Test
    public void testarMatchingMatematico() {
        // Simular os 512 bytes que o R307 envia
        byte[] bytesOriginais = new byte[512];
        new Random().nextBytes(bytesOriginais);
        
        // Converter para o formato que o SourceAFIS entende
        FingerprintTemplate template1 = new FingerprintTemplate().convert(bytesOriginais);
        FingerprintTemplate template2 = new FingerprintTemplate().convert(bytesOriginais); 

        // Comparar
        double score = new FingerprintMatcher(template1).match(template2);

        System.out.println("--- RESULTADO DO TESTE BIOMETRICO ---");
        System.out.println("Score de correspondência: " + score);
        
        if (score > 40) {
            System.out.println("Sucesso: Impressões digitais conferem!");
        } else {
            System.out.println("Falha: Impressões digitais diferentes.");
        }
    }
}

