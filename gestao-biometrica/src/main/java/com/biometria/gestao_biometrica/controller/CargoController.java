package com.biometria.gestao_biometrica.controller;

import com.biometria.gestao_biometrica.model.Cargo;
import com.biometria.gestao_biometrica.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*")
public class CargoController {

    @Autowired
    private CargoRepository cargoRepository;

    @GetMapping
    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }
}

