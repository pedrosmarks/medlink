package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy.AllergyCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation.ConsultationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis.DiagnosisCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication.MedicationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery.SurgeryCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine.VaccineCreateDto;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável pelo gerenciamento dos Prontuários Médicos.
 */
@RestController
@RequestMapping("/medical-records")
@CrossOrigin
public class MedicalRecordController extends BaseController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Cria um novo prontuário médico.
     * @param dto dados do prontuário a ser criado
     * @return resposta com dados do prontuário criado
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        entity.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Prontuário criado com sucesso!", MedicalRecordResponseDto.fromEntity(entity)));
    }

    /**
     * Lista todos os prontuários médicos cadastrados.
     * @return lista de prontuários
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDto>>> getAll() {
        List<MedicalRecord> records = medicalRecordService.findAll();
        List<MedicalRecordResponseDto> dtos = records.stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return success("Prontuários recuperados com sucesso.", dtos);
    }

    /**
     * Busca um prontuário médico pelo ID.
     * @param id identificador do prontuário
     * @return dados do prontuário encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> getById(@PathVariable int id) {
        validateId(id);
        
        MedicalRecord record = medicalRecordService.findById(id);
        if (record == null) {
            return notFound("Prontuário");
        }
        return success("Prontuário encontrado.", MedicalRecordResponseDto.fromEntity(record));
    }

    /**
     * Atualiza dados de um prontuário médico existente.
     * @param id identificador do prontuário
     * @param dto novos dados do prontuário
     * @return dados do prontuário atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> update(@PathVariable int id,
                                    @Valid @RequestBody MedicalRecordUpdateDto dto) {
        validateId(id);
        
        MedicalRecord entity = dto.toEntity();
        entity.setId(id);

        MedicalRecord updated = medicalRecordService.update(id, entity);
        if (updated == null) {
            return notFound("Prontuário");
        }
        return success("Prontuário atualizado com sucesso!", MedicalRecordResponseDto.fromEntity(updated));
    }

    /**
     * Remove um prontuário médico pelo ID.
     * @param id identificador do prontuário
     * @return confirmação da remoção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        validateId(id);
        
        boolean deleted = medicalRecordService.delete(id);
        if (!deleted) {
            return notFound("Prontuário para exclusão");
        }
        return success("Prontuário removido com sucesso.");
    }

    /**
     * Busca prontuário por paciente com verificação de permissão do médico.
     * @param medicId identificador do médico
     * @param patientId identificador do paciente
     * @return dados do prontuário do paciente
     */
    @GetMapping("/{medicId}/patients/{patientId}/medical-record")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> getMedicalRecordByPatient(
            @PathVariable int medicId,
            @PathVariable int patientId) {
        if (!isValidId(medicId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ID do médico deve ser maior que zero."));
        }
        if (!isValidId(patientId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("ID do paciente deve ser maior que zero."));
        }

        boolean hasPermission = true; // lógica fake
        if (!hasPermission) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("Acesso negado: você não tem permissão para este prontuário."));
        }

        MedicalRecordResponseDto dto = medicalRecordService.findByPatientId(medicId, patientId);
        if (dto == null) {
            return notFound("Prontuário do paciente");
        }
        return success("Prontuário encontrado.", dto);
    }

    /**
     * Adiciona consulta ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados da consulta
     * @return confirmação da adição
     */
    @PostMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<Void>> addConsultation(@PathVariable int id,
                                                             @Valid @RequestBody ConsultationCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addConsultation(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Consulta adicionada ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }

    /**
     * Adiciona medicamento ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados do medicamento
     * @return confirmação da adição
     */
    @PostMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<Void>> addMedication(@PathVariable int id,
                                                           @Valid @RequestBody MedicationCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addMedication(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Medicamento adicionado ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }

    /**
     * Adiciona alergia ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados da alergia
     * @return confirmação da adição
     */
    @PostMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<Void>> addAllergy(@PathVariable int id,
                                                        @Valid @RequestBody AllergyCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addAllergy(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Alergia adicionada ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }

    /**
     * Adiciona vacina ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados da vacina
     * @return confirmação da adição
     */
    @PostMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<Void>> addVaccine(@PathVariable int id,
                                                        @Valid @RequestBody VaccineCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addVaccine(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Vacina adicionada ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }

    /**
     * Adiciona cirurgia ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados da cirurgia
     * @return confirmação da adição
     */
    @PostMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<Void>> addSurgery(@PathVariable int id,
                                                        @Valid @RequestBody SurgeryCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addSurgery(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Cirurgia adicionada ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }

    /**
     * Adiciona diagnóstico ao prontuário.
     * @param id identificador do prontuário
     * @param dto dados do diagnóstico
     * @return confirmação da adição
     */
    @PostMapping("/{id}/diagnosis")
    public ResponseEntity<ApiResponse<Void>> addDiagnosis(@PathVariable int id,
                                                          @Valid @RequestBody DiagnosisCreateDto dto) {
        validateId(id);
        
        boolean success = medicalRecordService.addDiagnosis(id, dto.toEntity());
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Diagnóstico adicionado ao prontuário com sucesso."));
        } else {
            return internalServerError();
        }
    }
}
