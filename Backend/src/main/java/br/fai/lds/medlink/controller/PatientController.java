package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PacienteResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController

public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/api/pacientes")
    public ResponseEntity<List<PacienteResponseDto>> getAllPacientes() {
        try {
            List<Patient> patients = patientService.findAll();
            List<PacienteResponseDto> response = patients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/pacientes/{id}")
    public ResponseEntity<PacienteResponseDto> getPacienteById(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PacienteResponseDto.fromEntity(patient));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}