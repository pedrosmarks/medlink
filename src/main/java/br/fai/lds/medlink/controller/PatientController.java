package br.fai.lds.medlink.controller;


import br.fai.lds.medlink.domain.dto.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class PatientController {


    private final Patient patient;

    public PatientController(Patient patient) {
        this.patient = patient;
    }

    @GetMapping()
    public ResponseEntity<List<Patient>> getPatient(){
        return ResponseEntity.ok(patient.f
    }
}
