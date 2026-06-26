package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Horario; // Ajusta conforme o teu package real
import com.biometria.gestao_biometrica.repository.HorarioRepository; // Ajusta conforme o teu package real
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*") // Permite que o Angular aceda a isto
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    @GetMapping
    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }
}

