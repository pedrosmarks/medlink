package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
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

// Controlador responsável pelo gerenciamento dos Prontuários Médicos.
@RestController
@RequestMapping("/medical-records")
@CrossOrigin
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Operações CRUD básico no prontuário médico

    // Endpoint para criar um novo prontuario médico
    @PostMapping
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        entity.setId(id);

        ApiResponse<MedicalRecordResponseDto> response = new ApiResponse<>(
                "Prontuário criado com sucesso!",
                MedicalRecordResponseDto.fromEntity(entity)
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint para listar todos os prontuários médicos cadastrados
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> getAll() {
        List<MedicalRecord> records = medicalRecordService.findAll();
        List<MedicalRecordResponseDto> dtos = records.stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Endpoint para buscar um prontuário médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        MedicalRecord record = medicalRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Prontuário não encontrado para o ID: " + id));
        }
        return ResponseEntity.ok(MedicalRecordResponseDto.fromEntity(record));
    }

    // Endpoint para atualizar dados de um prontuário médico existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @Valid @RequestBody MedicalRecordUpdateDto dto) {
        MedicalRecord entity = dto.toEntity();
        entity.setId(id);

        MedicalRecord updated = medicalRecordService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Não foi possível atualizar. Prontuário não encontrado."));
        }

        ApiResponse<MedicalRecordResponseDto> response = new ApiResponse<>(
                "Prontuário atualizado com sucesso!",
                MedicalRecordResponseDto.fromEntity(updated)
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint para "excluir um prontuário médico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Prontuário não encontrado para exclusão."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Prontuário removido com sucesso."));
    }

    // Endpoint para buscar prontuário por paciente (e verificação de permissão do médico)
    @GetMapping("/{medicId}/patients/{patientId}/medical-record")
    public ResponseEntity<?> getMedicalRecordByPatient(
            @PathVariable int medicId,
            @PathVariable int patientId) {

        boolean hasPermission = true; // lógica fake

        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Acesso negado: você não tem permissão para este prontuário."));
        }

        MedicalRecordResponseDto dto = medicalRecordService.findByPatientId(medicId, patientId);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Prontuário do paciente não encontrado."));
        }

        return ResponseEntity.ok(dto);
    }

    // Endpoints para adicionar dados clínicos

    // Consultas
    @PostMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<Void>> addConsultation(@PathVariable int id,
                                                             @Valid @RequestBody ConsultationCreateDto dto) {
        medicalRecordService.addConsultation(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Consulta adicionada ao prontuário com sucesso."));
    }

    // Medicamentos
    @PostMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<Void>> addMedication(@PathVariable int id,
                                                           @Valid @RequestBody MedicationCreateDto dto) {
        medicalRecordService.addMedication(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Medicamento adicionado ao prontuário com sucesso."));
    }

    // Alergias
    @PostMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<Void>> addAllergy(@PathVariable int id,
                                                        @Valid @RequestBody AllergyCreateDto dto) {
        medicalRecordService.addAllergy(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Alergia adicionada ao prontuário com sucesso."));
    }

    //Vacinas
    @PostMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<Void>> addVaccine(@PathVariable int id,
                                                        @Valid @RequestBody VaccineCreateDto dto) {
        medicalRecordService.addVaccine(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Vacina adicionada ao prontuário com sucesso."));
    }

    //Cirurgia
    @PostMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<Void>> addSurgery(@PathVariable int id,
                                                        @Valid @RequestBody SurgeryCreateDto dto) {
        medicalRecordService.addSurgery(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Cirurgia adicionada ao prontuário com sucesso."));
    }

    //Diagnostico
    @PostMapping("/{id}/diagnosis")
    public ResponseEntity<ApiResponse<Void>> addDiagnosis(@PathVariable int id,
                                                          @Valid @RequestBody DiagnosisCreateDto dto) {
        medicalRecordService.addDiagnosis(id, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Diagnostico adicionado ao prontuário com sucesso."));
    }




}
