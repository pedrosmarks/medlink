package br.fai.lds.medlink.controller;


import br.fai.lds.medlink.domain.dto.Patient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PatientController {


    private final Patient patient;

    public PatientController(Patient patient) {
        this.patient = patient;
    }
}
