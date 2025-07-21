package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Diagnosis;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical.*;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // CRUD básico
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        entity.setId(id);
        return new ResponseEntity<>(MedicalRecordResponseDto.fromEntity(entity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> getAll() {
        List<MedicalRecord> records = medicalRecordService.findAll();
        List<MedicalRecordResponseDto> dtos = records.stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getById(@PathVariable int id) {
        MedicalRecord record = medicalRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MedicalRecordResponseDto.fromEntity(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(@PathVariable int id,
                                                           @Valid @RequestBody MedicalRecordUpdateDto dto) {

        MedicalRecord entity = dto.toEntity();
        entity.setId(id);

        MedicalRecord updated = medicalRecordService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MedicalRecordResponseDto.fromEntity(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // Buscar prontuário por paciente (e verificação de permissão do médico)
    @GetMapping("/{medicId}/patients/{patientId}/medical-record")
    public ResponseEntity<MedicalRecordResponseDto> getMedicalRecordByPatient(
            @PathVariable int medicId,
            @PathVariable int patientId) {

        boolean hasPermission = true; // lógica fake

        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        MedicalRecordResponseDto dto = medicalRecordService.findByPatientId(medicId, patientId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    // NOVOS ENDPOINTS CLÍNICOS

    @PostMapping("/{id}/consultations")
    public ResponseEntity<Void> addConsultation(@PathVariable int id,
                                                @Valid @RequestBody ConsultationCreateDto dto) {
        medicalRecordService.addConsultation(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/medications")
    public ResponseEntity<Void> addMedication(@PathVariable int id,
                                              @Valid @RequestBody MedicationCreateDto dto) {
        medicalRecordService.addMedication(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/allergies")
    public ResponseEntity<Void> addAllergy(@PathVariable int id,
                                           @Valid @RequestBody AllergyCreateDto dto) {
        medicalRecordService.addAllergy(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/vaccines")
    public ResponseEntity<Void> addVaccine(@PathVariable int id,
                                           @Valid @RequestBody VaccineCreateDto dto) {
        medicalRecordService.addVaccine(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/surgeries")
    public ResponseEntity<Void> addSurgery(@PathVariable int id,
                                           @Valid @RequestBody SurgeryCreateDto dto) {
        medicalRecordService.addSurgery(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/family-history")
    public ResponseEntity<Void> addDiagnosis(@PathVariable int id,
                                                 @Valid @RequestBody DiagnosisCreateDto dto) {
        medicalRecordService.addFamilyHistory(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
