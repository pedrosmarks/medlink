package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {


    private final PatientService patientService;;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List<Patient>> getPatient(){
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getEntityById(@PathVariable final int id){
        Patient entity = patientService.findById(id);


        return entity == null ?
                ResponseEntity.notFound().build():ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Patient> createNew(@RequestBody final Patient data) {
        final int id = patientService.create(data);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(data);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean result = patientService.deactivate(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}