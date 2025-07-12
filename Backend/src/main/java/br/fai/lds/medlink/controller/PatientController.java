package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    //Retorna todos os pacientes cadastrados
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAll() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponseDto> dtoList = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    //Retorna um paciente específico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getById(@PathVariable final int id) {
        Patient entity = patientService.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        PatientResponseDto dto = PatientResponseDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }

    //Cria um novo paciente
    @PostMapping
    public ResponseEntity<PatientResponseDto> create(@Valid @RequestBody PatientCreateDto dto) {
        Patient entity = dto.toEntity();
        int id = patientService.create(entity);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        PatientResponseDto responseDto = PatientResponseDto.fromEntity(entity);

        return ResponseEntity.created(location).body(responseDto);
    }

    // Desativa (inativa) um paciente pelo ID

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean result = patientService.deactivate(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
