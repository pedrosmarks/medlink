package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medics")
public class MedicController {

    private final MedicService medicService;
    private final PatientService patientService; //

    public MedicController(MedicService medicService, PatientService patientService) {
        this.medicService = medicService;
        this.patientService = patientService; //
    }

    @PostMapping
    public ResponseEntity<MedicResponseDto> createMedic(@Valid @RequestBody MedicCreateDto dto) {
        Medic medic = dto.toEntity();
        int id = medicService.create(medic);
        medic.setId(id);
        return new ResponseEntity<>(MedicResponseDto.fromEntity(medic), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicResponseDto>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtos = medics.stream()
                .map(MedicResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicResponseDto> getMedicById(@PathVariable int id) {
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MedicResponseDto.fromEntity(medic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicResponseDto> updateMedic(@PathVariable int id,
                                                        @Valid @RequestBody MedicUpdateDto dto) {
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return ResponseEntity.notFound().build();
        }
        dto.updateEntity(medic);
        Medic updated = medicService.update(id, medic);
        return ResponseEntity.ok(MedicResponseDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    //  Listar pacientes vinculados ao médico
    @GetMapping("/{id}/patients")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByMedic(@PathVariable("id") int medicId) {
        List<Patient> patients = patientService.findByMedicId(medicId); // busca entidades
        List<PatientResponseDto> dtos = patients.stream()
                .map(PatientResponseDto::fromEntity) // converte para DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
