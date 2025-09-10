package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.AccessRequestResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.AuthorizedDoctorDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.DiagnosisCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PacienteResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.RequisicaoAcessoDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.SurgeryCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.VaccineCreateDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import br.fai.lds.medlink.util.LogSanitizer;

@RestController
@CrossOrigin
@RequestMapping("api/patients")
public class PatientController extends BaseController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicService medicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteResponseDto>>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PacienteResponseDto> response = patients.stream()
                .map(PacienteResponseDto::fromEntity)
                .toList();
        return success("Pacientes listados com sucesso.", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDto>> getPatientById(@PathVariable int id) {
        validateId(id);
        
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return notFound("Paciente");
        }
        return success("Paciente encontrado com sucesso.", PacienteResponseDto.fromEntity(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDto>> updatePatient(@PathVariable int id, @Valid @RequestBody Patient patient) {
        validateId(id);
        
        Patient existingPatient = patientService.findById(id);
        if (existingPatient == null) {
            return notFound("Paciente");
        }

        Patient updatedPatient = patientService.update(id, patient);
        return success("Paciente atualizado com sucesso.", PacienteResponseDto.fromEntity(updatedPatient));
    }

    // Endpoints para buscar dados médicos específicos do paciente
    
    @GetMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<List<Consultation>>> getConsultationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getConsultations, "Consultas recuperadas com sucesso.");
    }

    @GetMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<List<Vaccine>>> getVaccinesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getVacinas, "Vacinas recuperadas com sucesso.");
    }

    @PostMapping("/{patientId}/vaccines")
    public ResponseEntity<ApiResponse<String>> addVaccine(
            @PathVariable int patientId,
            @Valid @RequestBody VaccineCreateDto vaccineDto) {
        return addMedicalItem(patientId, "vacina", (patient) -> {
            int newId = generateNextId(patient.getVacinas(), Vaccine::getId);
            Vaccine newVaccine = new Vaccine(newId, vaccineDto.getName(), vaccineDto.getDate());
            patient.getVacinas().add(newVaccine);
            return patient;
        });
    }

    @DeleteMapping("/{patientId}/vaccines/{vaccineId}")
    public ResponseEntity<ApiResponse<String>> deleteVaccine(
            @PathVariable int patientId,
            @PathVariable int vaccineId) {
        return removeMedicalItem(patientId, vaccineId, "vacina", patient -> 
            patient.getVacinas().removeIf(vaccine -> vaccine.getId() == vaccineId)
        );
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<List<Medication>>> getMedicationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getMedications, "Medicamentos recuperados com sucesso.");
    }

    // Add medication to patient
    @PostMapping("/{patientId}/medications")
    public ResponseEntity<ApiResponse<String>> addMedication(
            @PathVariable int patientId,
            @Valid @RequestBody Medication medicationDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            patient.getMedications().add(medicationDto);
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Medicamento adicionado com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar medicamento para paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Delete medication from patient
    @DeleteMapping("/{patientId}/medications/{medicationName}")
    public ResponseEntity<ApiResponse<String>> deleteMedication(
            @PathVariable int patientId,
            @PathVariable String medicationName) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            boolean removed = patient.getMedications().removeIf(medication -> 
                medication.getName() != null && medication.getName().equals(medicationName));
            
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Medicamento não encontrado."));
            }
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Medicamento removido com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao remover medicamento {} do paciente ID {}: {}", LogSanitizer.sanitizeAndLimit(medicationName, 50), LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<List<Surgery>>> getSurgeriesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getCirurgias, "Cirurgias recuperadas com sucesso.");
    }

    @PostMapping("/{patientId}/surgeries")
    public ResponseEntity<ApiResponse<String>> addSurgery(
            @PathVariable int patientId,
            @Valid @RequestBody SurgeryCreateDto surgeryDto) {
        return addMedicalItem(patientId, "cirurgia", (patient) -> {
            int newId = generateNextId(patient.getCirurgias(), Surgery::getId);
            Surgery newSurgery = new Surgery(newId, surgeryDto.getName(), 
                surgeryDto.getDate(), surgeryDto.getLocation(), surgeryDto.getNotes());
            patient.getCirurgias().add(newSurgery);
            return patient;
        });
    }

    @DeleteMapping("/{patientId}/surgeries/{surgeryId}")
    public ResponseEntity<ApiResponse<String>> deleteSurgery(
            @PathVariable int patientId,
            @PathVariable int surgeryId) {
        return removeMedicalItem(patientId, surgeryId, "cirurgia", patient -> 
            patient.getCirurgias().removeIf(surgery -> surgery.getId() == surgeryId)
        );
    }

    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<ApiResponse<List<Diagnosis>>> getDiagnosesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getDiagnosticos, "Diagnósticos recuperados com sucesso.");
    }

    // Add diagnosis to patient
    @PostMapping("/{patientId}/diagnoses")
    public ResponseEntity<ApiResponse<String>> addDiagnosis(
            @PathVariable int patientId,
            @Valid @RequestBody DiagnosisCreateDto diagnosisDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Generate new ID for diagnosis using existing helper method
            int newDiagnosisId = generateNextId(patient.getDiagnosticos(), Diagnosis::getId);
            
            Diagnosis newDiagnosis = new Diagnosis(newDiagnosisId, diagnosisDto.getDescription(), diagnosisDto.getDate());
            patient.getDiagnosticos().add(newDiagnosis);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Diagnóstico adicionado com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar diagnóstico para paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Delete diagnosis from patient
    @DeleteMapping("/{patientId}/diagnoses/{diagnosisId}")
    public ResponseEntity<ApiResponse<String>> deleteDiagnosis(
            @PathVariable int patientId,
            @PathVariable int diagnosisId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            boolean removed = patient.getDiagnosticos().removeIf(diagnosis -> diagnosis.getId() == diagnosisId);
            
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Diagnóstico não encontrado."));
            }
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Diagnóstico removido com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao remover diagnóstico ID {} do paciente ID {}: {}", LogSanitizer.sanitizeId(diagnosisId), LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<List<Allergy>>> getAllergiesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getAlergias, "Alergias recuperadas com sucesso.");
    }

    // Add allergy to patient
    @PostMapping("/{patientId}/allergies")
    public ResponseEntity<ApiResponse<String>> addAllergy(
            @PathVariable int patientId,
            @Valid @RequestBody Allergy allergyDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Generate new ID for allergy using existing helper method
            int newAllergyId = generateNextId(patient.getAlergias(), Allergy::getId);
            
            allergyDto.setId(newAllergyId);
            patient.getAlergias().add(allergyDto);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Alergia adicionada com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar alergia para paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Delete allergy from patient
    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<ApiResponse<String>> deleteAllergy(
            @PathVariable int patientId,
            @PathVariable int allergyId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            boolean removed = patient.getAlergias().removeIf(allergy -> allergy.getId() == allergyId);
            
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Alergia não encontrada."));
            }
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Alergia removida com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao remover alergia ID {} do paciente ID {}: {}", LogSanitizer.sanitizeId(allergyId), LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para buscar pacientes por nome
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PacienteResponseDto>>> searchPatients(@RequestParam String name) {
        try {
            List<Patient> allPatients = patientService.findAll();
            List<Patient> filteredPatients = allPatients.stream()
                    .filter(patient -> patient.getName() != null && patient.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
            
            List<PacienteResponseDto> response = filteredPatients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .toList();
            
            return ResponseEntity.ok(new ApiResponse<>("Pacientes encontrados.", response));
        } catch (Exception e) {
            log.error("Erro ao buscar pacientes por nome '{}': {}", LogSanitizer.sanitizeAndLimit(name, 30), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Send access request to patient
    @PostMapping("/{patientId}/access-request")
    public ResponseEntity<ApiResponse<String>> sendAccessRequest(
            @PathVariable int patientId, 
            @Valid @RequestBody RequisicaoAcessoDto request) {
        try {
            patientService.sendAccessRequest(patientId, request.getMedicoId());
            return ResponseEntity.ok(new ApiResponse<>("Requisição de acesso enviada com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao enviar requisição de acesso para paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Get pending access requests (for notifications)
    @GetMapping("/{patientId}/pending-requests")
    public ResponseEntity<ApiResponse<List<AccessRequestResponseDto>>> getPendingRequests(@PathVariable int patientId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Collect all medic IDs first to avoid N+1 queries
            List<Integer> medicIds = patient.getRequisicoesAcesso().stream()
                    .filter(req -> "PENDENTE".equals(req.getStatus()))
                    .map(RequisicaoAcesso::getMedicoId)
                    .distinct()
                    .collect(Collectors.toList());
            
            // Batch fetch all medics at once using optimized method
            Map<Integer, Medic> medicsMap = medicService.findByIds(medicIds);
            
            List<AccessRequestResponseDto> pendingRequests = patient.getRequisicoesAcesso().stream()
                    .filter(req -> "PENDENTE".equals(req.getStatus()))
                    .map(req -> {
                        Medic medic = medicsMap.get(req.getMedicoId());
                        return new AccessRequestResponseDto(
                            req.getMedicoId(),
                            medic != null ? medic.getName() : "Médico não encontrado",
                            medic != null ? medic.getSpecialty() : "Especialidade não informada",
                            req.getStatus()
                        );
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>("Requisições pendentes recuperadas.", pendingRequests));
        } catch (Exception e) {
            log.error("Erro ao buscar requisições pendentes do paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @PutMapping("/{patientId}/access-request/{medicId}")
    public ResponseEntity<ApiResponse<String>> updateAccessRequest(
            @PathVariable int patientId,
            @PathVariable int medicId,
            @RequestParam String action) {
        try {
            // Atualiza status da requisição
            String newStatus = "approve".equals(action) ? "ACEITA" : "RECUSADA";
            patientService.updateAccessRequestStatus(patientId, medicId, newStatus);

            // Se for aprovação, vincula o médico ao paciente
            if ("approve".equals(action)) {
                Patient patient = findPatientOrThrow(patientId);
                authorizeSpecialist(patient, medicId, patientId);
                patientService.update(patientId, patient);
            }
            String message = getSuccessMessage(action);
            return ResponseEntity.ok(new ApiResponse<>(message));
        } catch (Exception e) {
            log.error("Erro ao atualizar requisição de acesso: {}", LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    private Patient findPatientOrThrow(int patientId) {
        Patient patient = patientService.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }
        return patient;
    }

    /**
     * Método helper para operações que precisam validar paciente
     */
    private <T> ResponseEntity<ApiResponse<T>> executePatientOperation(
            int patientId, 
            java.util.function.Function<Patient, ResponseEntity<ApiResponse<T>>> operation) {
        validateId(patientId);
        
        Patient patient = patientService.findById(patientId);
        if (patient == null) {
            return notFound("Paciente");
        }
        return operation.apply(patient);
    }

    private RequisicaoAcesso findPendingRequestOrThrow(Patient patient, int medicId) {
    return patient.getRequisicoesAcesso().stream()
        .filter(req -> req.getMedicoId() == medicId && "PENDENTE".equals(req.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Requisição não encontrada."));
    }

    private void updateRequestStatus(RequisicaoAcesso request, String action) {
    String newStatus = "approve".equals(action) ? "ACEITA" : "RECUSADA";
    request.setStatus(newStatus);
    }

    private void authorizeSpecialist(Patient patient, int medicId, int patientId) {
        if (patient.getEspecialistasAutorizados() == null) {
            patient.setEspecialistasAutorizados(new ArrayList<>());
        }
        boolean alreadyAuthorized = patient.getEspecialistasAutorizados().stream()
                .anyMatch(esp -> Objects.equals(esp.getMedicoId(), (long) medicId));
        if (!alreadyAuthorized) {
            patient.getEspecialistasAutorizados().add(new EspecialistaAutorizado((long) medicId));
            log.info("Médico {} autorizado para paciente {}", LogSanitizer.sanitizeId(medicId), LogSanitizer.sanitizeId(patientId));
            // Persistir vínculo no banco via service
            patientService.authorizeSpecialist(patientId, medicId);
        }
    }

    private String getSuccessMessage(String action) {
        return "approve".equals(action) ? 
            "Acesso aprovado com sucesso. Médico vinculado ao paciente." : 
            "Acesso rejeitado com sucesso.";
    }

    private ResponseEntity<ApiResponse<String>> addMedicalItem(int patientId, String itemType, 
            java.util.function.Function<Patient, Patient> addOperation) {
        try {
            Patient patient = findPatientOrThrow(patientId);
            Patient updatedPatient = addOperation.apply(patient);
            patientService.update(patientId, updatedPatient);
            return ResponseEntity.ok(new ApiResponse<>(itemType + " adicionada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao adicionar {} para paciente ID {}: {}", LogSanitizer.sanitizeAndLimit(itemType, 20), LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    private ResponseEntity<ApiResponse<String>> removeMedicalItem(int patientId, int itemId, 
            String itemType, java.util.function.Function<Patient, Boolean> removeOperation) {
        try {
            Patient patient = findPatientOrThrow(patientId);
            boolean removed = removeOperation.apply(patient);
            
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(itemType + " não encontrada."));
            }
            
            patientService.update(patientId, patient);
            return ResponseEntity.ok(new ApiResponse<>(itemType + " removida com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao remover {} ID {} do paciente ID {}: {}", LogSanitizer.sanitizeAndLimit(itemType, 20), LogSanitizer.sanitizeId(itemId), LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    private <T> int generateNextId(java.util.List<T> items, java.util.function.ToIntFunction<T> idExtractor) {
        return items.stream().mapToInt(idExtractor).max().orElse(0) + 1;
    }

    // DEBUG: Verificar dados do paciente
    @GetMapping("/{patientId}/debug")
    public ResponseEntity<String> debugPatient(@PathVariable int patientId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.ok("Paciente não encontrado");
            }
            StringBuilder debug = new StringBuilder();
            debug.append("Paciente: ").append(patient.getName()).append("\n");
            debug.append("ID: ").append(patient.getId()).append("\n");
            debug.append("MedicId: ").append(patient.getMedicId()).append("\n");
            debug.append("Especialistas autorizados: ");
            if (patient.getEspecialistasAutorizados() != null) {
                debug.append(patient.getEspecialistasAutorizados().size()).append("\n");
                for (EspecialistaAutorizado esp : patient.getEspecialistasAutorizados()) {
                    debug.append("  - Médico ID: ").append(esp.getMedicoId()).append("\n");
                }
            }
            return ResponseEntity.ok(debug.toString());
        } catch (Exception e) {
            log.error("Erro ao buscar dados do paciente ID {}: {}", LogSanitizer.sanitizeId(patientId), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor.");
        }
    }

    // DEBUG: Listar todos os pacientes com seus médicos
    @GetMapping("/debug/all")
    public ResponseEntity<String> debugAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            StringBuilder debug = new StringBuilder();
            debug.append("Total de pacientes: ").append(patients.size()).append("\n\n");
            
            for (Patient p : patients) {
                debug.append("Paciente: ").append(p.getName()).append(" (ID: ").append(p.getId()).append(")\n");
                debug.append("  MedicId: ").append(p.getMedicId()).append("\n");
                debug.append("  Especialistas: ");
                if (p.getEspecialistasAutorizados() != null) {
                    debug.append(p.getEspecialistasAutorizados().size()).append("\n");
                    for (EspecialistaAutorizado esp : p.getEspecialistasAutorizados()) {
                        debug.append("    - Médico ID: ").append(esp.getMedicoId()).append("\n");
                    }
                } else {
                    debug.append("null\n");
                }
                debug.append("\n");
            }
            
            return ResponseEntity.ok(debug.toString());
        } catch (Exception e) {
            return ResponseEntity.ok("Erro: " + e.getMessage());
        }
    }

    // Método auxiliar para reduzir duplicação de código
    private <T> ResponseEntity<ApiResponse<List<T>>> getPatientMedicalData(int id, java.util.function.Function<Patient, List<T>> dataExtractor, String successMessage) {
        return executePatientOperation(id, patient -> {
            List<T> data = dataExtractor.apply(patient);
            return success(successMessage, data);
        });
    }

    // Endpoint para listar médicos autorizados de um paciente
    @GetMapping("/{id}/authorized-doctors")
    public ResponseEntity<ApiResponse<List<AuthorizedDoctorDto>>> getAuthorizedDoctors(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            List<RequisicaoAcesso> requisicoes = patient.getRequisicoesAcesso();
            if (requisicoes == null || requisicoes.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>("Médicos autorizados recuperados com sucesso.", new ArrayList<>()));
            }
            
            // Collect all authorized medic IDs to avoid N+1 queries
            List<Integer> authorizedMedicIds = requisicoes.stream()
                    .filter(req -> "ACEITA".equals(req.getStatus()))
                    .map(RequisicaoAcesso::getMedicoId)
                    .distinct()
                    .collect(Collectors.toList());
            
            // Batch fetch all authorized medics at once using optimized method
            Map<Integer, Medic> medicsMap = medicService.findByIds(authorizedMedicIds);
            
            // Build response using cached medics
            List<AuthorizedDoctorDto> authorizedDoctors = authorizedMedicIds.stream()
                    .map(medicsMap::get)
                    .filter(Objects::nonNull)
                    .map(medic -> new AuthorizedDoctorDto(medic.getId(), medic.getName(), medic.getSpecialty(), medic.getCrm()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>("Médicos autorizados recuperados com sucesso.", authorizedDoctors));
        } catch (Exception e) {
            log.error("Erro ao buscar médicos autorizados do paciente ID {}: {}", LogSanitizer.sanitizeId(id), LogSanitizer.sanitize(e.getMessage()), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}