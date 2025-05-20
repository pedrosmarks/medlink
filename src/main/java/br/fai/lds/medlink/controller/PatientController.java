package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.dto.PatientDto;
import br.fai.lds.medlink.mapper.PatientMapper;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {


    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(PatientService patientService, PatientMapper patientMapper) {

        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @GetMapping()
    public ResponseEntity<List<PatientDto>> getPatient() {

        List<Patient> patients = patientService.findAll();
        List<PatientDto> dtoList = patients.stream()
                .map(patientMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getEntityById(@PathVariable final int id) {
        Patient entity = patientService.findById(id);

        if (entity == null) {
            ResponseEntity.notFound().build();
        }

        PatientDto dto = patientMapper.toDto(entity);
        return ResponseEntity.ok(dto);

    }

    @PostMapping
    public ResponseEntity<PatientDto> createNew(@RequestBody PatientDto dto) {
        Patient entity = patientMapper.toEntity(dto);
        int id = patientService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean result = patientService.deactivate(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}