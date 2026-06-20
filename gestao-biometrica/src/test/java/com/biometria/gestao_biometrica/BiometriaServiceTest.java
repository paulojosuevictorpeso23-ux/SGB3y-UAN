package com.biometria.gestao_biometrica;

import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.model.Utilizador;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import com.biometria.gestao_biometrica.service.BiometriaService;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Base64;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BiometriaServiceTest {

    @Test
    public void testarIdentificacaoSucesso() {
        // 1. Criar um template válido (vazio, mas com formato correto para a biblioteca)
        FingerprintTemplate templateReal = new FingerprintTemplate();
        String base64Template = Base64.getEncoder().encodeToString(templateReal.toByteArray());

        // 2. Simular utilizador
        Utilizador user = new Utilizador();
        user.setNome("Estudante Teste");
        
        Biometria b = new Biometria();
        b.setTemplateBiometrico(base64Template);
        b.setUtilizador(user);

        // 3. Mock do repositório
        BiometriaRepository repo = Mockito.mock(BiometriaRepository.class);
        when(repo.findAll()).thenReturn(Collections.singletonList(b));

        // 4. Injetar o mock no serviço
        BiometriaService service = new BiometriaService(repo);
        
        // 5. Executar o teste
        String resultado = service.identificarUtilizador(base64Template);

        assertEquals("Estudante Teste", resultado);
    }
}

