package com.biometria.gestao_biometrica.service;

import com.biometria.gestao_biometrica.model.Biometria;
import com.biometria.gestao_biometrica.repository.BiometriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BiometriaService {

    @Autowired
    private BiometriaRepository biometriaRepository;

    public Biometria salvar(Biometria biometria) {
        return biometriaRepository.save(biometria);
    }

    public List<Biometria> listarTodas() {
        return biometriaRepository.findAll();
    }

    public Optional<Biometria> buscarPorId(Long id) {
        return biometriaRepository.findById(id);
    }

    public List<Biometria> buscarPorUtilizador(Long utilizadorId) {
        return biometriaRepository.findByUtilizadorId(utilizadorId);
    }

    public void eliminar(Long id) {
        biometriaRepository.deleteById(id);
    }
}
