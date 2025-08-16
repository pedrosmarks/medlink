package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.service.medic.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfil")
@CrossOrigin
public class PerfilController {

    private final MedicService medicService;

    @Autowired
    public PerfilController(MedicService medicService) {
        this.medicService = medicService;
    }

    @GetMapping
    public List<Medic> getPerfis() {
        return medicService.findAll();
    }

    @GetMapping("/{id}")
    public Medic getPerfilById(@PathVariable int id) {
        return medicService.findById(id);
    }

    @PutMapping("/{id}")
    public Medic updatePerfil(@PathVariable int id, @RequestBody Medic perfil) {
        return medicService.update(id, perfil);
    }
}