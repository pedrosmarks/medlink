package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
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

    // Criar prontuário médico
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        entity.setId(id);
        return new ResponseEntity<>(MedicalRecordResponseDto.fromEntity(entity), HttpStatus.CREATED);
    }

    // Listar todos os prontuários
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> getAll() {
        List<MedicalRecord> records = medicalRecordService.findAll();
        List<MedicalRecordResponseDto> dtos = records.stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar prontuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getById(@PathVariable int id) {
        MedicalRecord record = medicalRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MedicalRecordResponseDto.fromEntity(record));
    }

    // Atualizar prontuário médico
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(@PathVariable int id,
                                                           @Valid @RequestBody MedicalRecordUpdateDto dto) {
        MedicalRecord record = medicalRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        dto.updateEntity(record);
        MedicalRecord updated = medicalRecordService.update(id, record);
        return ResponseEntity.ok(MedicalRecordResponseDto.fromEntity(updated));
    }

    // Inativar prontuário médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{medicId}/patients/{patientId}/medical-record")
    public ResponseEntity<MedicalRecordResponseDto> getMedicalRecordByPatient(
            @PathVariable int medicId,
            @PathVariable int patientId) {

        boolean hasPermission = true;

        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        MedicalRecordResponseDto dto = medicalRecordService.findByPatientId(medicId, patientId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }



}
