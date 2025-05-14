package br.fai.lds.medlink.controller;


import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class PatientController {


    private final PatientService patientService;;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public ResponseEntity<List<Patient>> getPatient(){
        return ResponseEntity.ok(patientService.findAll());
    }
}