package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.service.medic.MedicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medic")
public class MedicController {

    private final MedicService medicService;

    public MedicController(MedicService medicService) {

        this.medicService = medicService;
    }

    @GetMapping
    ResponseEntity<List<Medic>> getMedic(){

        return ResponseEntity.ok(medicService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medic> getEntityById(@PathVariable final int id){
        Medic entity = medicService.findById(id);


        return entity == null ?
                ResponseEntity.notFound().build():ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Medic> createNew(@RequestBody final Medic data) {
        final int id = medicService.create(data);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(data);
    }

    @PutMapping("/{id}/remove")
    public ResponseEntity<Void> deleteMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
