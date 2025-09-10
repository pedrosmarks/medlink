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

// Controlador responsável pelo gerenciamento dos Prontuários Médicos.
@RestController
@RequestMapping("/medical-records")
@CrossOrigin
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Endpoint para criar um novo prontuario médico
    @PostMapping
    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        try {
            MedicalRecord entity = dto.toEntity();
            int id = medicalRecordService.create(entity);
            entity.setId(id);

            ApiResponse<MedicalRecordResponseDto> response = new ApiResponse<>(
                    "Prontuário criado com sucesso!",
                    MedicalRecordResponseDto.fromEntity(entity)
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para listar todos os prontuários médicos cadastrados
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDto>>> getAll() {
        try {
            List<MedicalRecord> records = medicalRecordService.findAll();
            List<MedicalRecordResponseDto> dtos = records.stream()
                    .map(MedicalRecordResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("Prontuários recuperados com sucesso.", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para buscar um prontuário médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            MedicalRecord record = medicalRecordService.findById(id);
            if (record == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Prontuário não encontrado para o ID: " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>("Prontuário encontrado.", MedicalRecordResponseDto.fromEntity(record)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para atualizar dados de um prontuário médico existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @Valid @RequestBody MedicalRecordUpdateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para "excluir um prontuário médico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean deleted = medicalRecordService.delete(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Prontuário não encontrado para exclusão."));
            }
            return ResponseEntity.ok(new ApiResponse<>("Prontuário removido com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para buscar prontuário por paciente (e verificação de permissão do médico)
    @GetMapping("/{medicId}/patients/{patientId}/medical-record")
    public ResponseEntity<?> getMedicalRecordByPatient(
            @PathVariable int medicId,
            @PathVariable int patientId) {
        try {
            if (medicId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID do médico deve ser maior que zero."));
            }
            if (patientId <= 0) {
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Prontuário do paciente não encontrado."));
            }

            return ResponseEntity.ok(new ApiResponse<>("Prontuário encontrado.", dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Consultas
    @PostMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<Void>> addConsultation(@PathVariable int id,
                                                             @Valid @RequestBody ConsultationCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addConsultation(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Consulta adicionada ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar consulta ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Medicamentos
    @PostMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<Void>> addMedication(@PathVariable int id,
                                                           @Valid @RequestBody MedicationCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addMedication(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Medicamento adicionado ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar medicamento ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Alergias
    @PostMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<Void>> addAllergy(@PathVariable int id,
                                                        @Valid @RequestBody AllergyCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addAllergy(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Alergia adicionada ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar alergia ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Vacinas
    @PostMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<Void>> addVaccine(@PathVariable int id,
                                                        @Valid @RequestBody VaccineCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addVaccine(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Vacina adicionada ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar vacina ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Cirurgia
    @PostMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<Void>> addSurgery(@PathVariable int id,
                                                        @Valid @RequestBody SurgeryCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addSurgery(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Cirurgia adicionada ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar cirurgia ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Diagnóstico
    @PostMapping("/{id}/diagnosis")
    public ResponseEntity<ApiResponse<Void>> addDiagnosis(@PathVariable int id,
                                                          @Valid @RequestBody DiagnosisCreateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            boolean success = medicalRecordService.addDiagnosis(id, dto.toEntity());
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse<>("Diagnostico adicionado ao prontuário com sucesso."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>("Erro ao adicionar diagnóstico ao prontuário."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Dados inválidos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}
