package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Criar paciente
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientCreateDto dto) {
        Patient patient = dto.toEntity();
        int id = patientService.create(patient);
        patient.setId(id);
        return new ResponseEntity<>(PatientResponseDto.fromEntity(patient), HttpStatus.CREATED);
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponseDto> dtos = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable int id) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PatientResponseDto.fromEntity(patient));
    }

    // Atualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable int id,
                                                            @Valid @RequestBody PatientUpdateDto dto) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        dto.updateEntity(patient);
        Patient updated = patientService.update(id, patient);
        return ResponseEntity.ok(PatientResponseDto.fromEntity(updated));
    }

    // Inativar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivatePatient(@PathVariable int id) {
        boolean success = patientService.deactivate(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
