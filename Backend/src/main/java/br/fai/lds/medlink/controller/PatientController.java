package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.Access.AccessRequestResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Access.AuthorizedDoctorDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis.DiagnosisCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery.SurgeryCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine.VaccineCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine.VaccineResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Access.AccessRequestDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import br.fai.lds.medlink.util.LogSanitizer;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para gerenciamento de pacientes e seus dados médicos.
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, 
           methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/patients")
public class PatientController extends BaseController {

    public PatientController() {
        log.info("✓ PatientController inicializado - Endpoint /api/patients/{patientId}/access-requests disponível");
    }

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicService medicService;

    /**
     * Lista todos os pacientes.
     * @return lista de pacientes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponseDto> response = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .toList();
        return success("Pacientes listados com sucesso.", response);
    }

    /**
     * Busca paciente por ID.
     * @param id identificador do paciente
     * @return dados do paciente
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDto>> getPatientById(@PathVariable int id) {
        validateId(id);
        
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return notFound("Paciente");
        }
        return success("Paciente encontrado com sucesso.", PatientResponseDto.fromEntity(patient));
    }

    /**
     * Atualiza dados do paciente.
     * @param id identificador do paciente
     * @param patient novos dados do paciente
     * @return dados do paciente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDto>> updatePatient(@PathVariable int id, @Valid @RequestBody Patient patient) {
        validateId(id);
        
        Patient existingPatient = patientService.findById(id);
        if (existingPatient == null) {
            return notFound("Paciente");
        }

        Patient updatedPatient = patientService.update(id, patient);
        return success("Paciente atualizado com sucesso.", PatientResponseDto.fromEntity(updatedPatient));
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
        log.info("Iniciando adição de vacina para paciente ID: {}", LogSanitizer.sanitizeId(patientId));
        log.debug("Dados da vacina recebidos: {}", vaccineDto);
        
        try {
            return addMedicalItem(patientId, "vacina", (patient) -> {
                log.debug("Paciente encontrado: {}", patient.getName());
                log.debug("Vacinas atuais: {}", patient.getVacinas().size());
                
                int newId = generateNextId(patient.getVacinas(), Vaccine::getId);
                log.debug("Novo ID gerado para vacina: {}", newId);
                
                Vaccine newVaccine = Vaccine.builder()
                    .id(newId)
                    .name(vaccineDto.getName())
                    .date(vaccineDto.getDate())
                    .build();
                log.debug("Nova vacina criada: {}", newVaccine);
                
                patient.getVacinas().add(newVaccine);
                log.debug("Vacina adicionada. Total de vacinas: {}", patient.getVacinas().size());
                
                return patient;
            });
        } catch (Exception e) {
            log.error("Erro ao adicionar vacina para paciente {}: {}", LogSanitizer.sanitizeId(patientId), e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{patientId}/vaccines/{vaccineId}")
    public ResponseEntity<ApiResponse<String>> deleteVaccine(
            @PathVariable int patientId,
            @PathVariable int vaccineId) {
        log.info("Iniciando remoção de vacina ID {} do paciente {}", vaccineId, patientId);
        return removeMedicalItem(patientId, vaccineId, "vacina", patient -> {
            log.debug("Vacinas antes da remoção: {}", patient.getVacinas().size());
            boolean removed = patient.getVacinas().removeIf(vaccine -> vaccine.getId() == vaccineId);
            log.debug("Vacina removida: {}, Vacinas após remoção: {}", removed, patient.getVacinas().size());
            return removed;
        });
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<List<Medication>>> getMedicationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getMedications, "Medicamentos recuperados com sucesso.");
    }

    @PostMapping("/{patientId}/medications")
    public ResponseEntity<ApiResponse<String>> addMedication(
            @PathVariable int patientId,
            @Valid @RequestBody Medication medicationDto) {
        log.info("Iniciando adição de medicamento para paciente ID: {}", patientId);
        log.debug("Dados do medicamento recebidos: {}", medicationDto);
        
        return addMedicalItem(patientId, "medicamento", (patient) -> {
            log.debug("Medicamentos atuais: {}", patient.getMedications().size());
            
            // ID será gerado automaticamente pelo banco
            patient.getMedications().add(medicationDto);
            
            log.debug("Medicamento adicionado. Total de medicamentos: {}", patient.getMedications().size());
            return patient;
        });
    }

    @DeleteMapping("/{patientId}/medications/{medicationId}")
    public ResponseEntity<ApiResponse<String>> deleteMedication(
            @PathVariable int patientId,
            @PathVariable int medicationId) {
        log.info("Iniciando remoção de medicamento ID {} do paciente {}", medicationId, patientId);
        return removeMedicalItem(patientId, medicationId, "medicamento", patient -> {
            log.debug("Medicamentos antes da remoção: {}", patient.getMedications().size());
            boolean removed = patient.getMedications().removeIf(medication -> 
                medication.getId() != null && medication.getId() == medicationId);
            log.debug("Medicamento removido: {}, Medicamentos após remoção: {}", removed, patient.getMedications().size());
            return removed;
        });
    }

    @GetMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<List<Surgery>>> getSurgeriesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getCirurgias, "Cirurgias recuperadas com sucesso.");
    }

    @PostMapping("/{patientId}/surgeries")
    public ResponseEntity<ApiResponse<String>> addSurgery(
            @PathVariable int patientId,
            @Valid @RequestBody SurgeryCreateDto surgeryDto) {
        log.info("Iniciando adição de cirurgia para paciente ID: {}", LogSanitizer.sanitizeId(patientId));
        log.debug("Dados da cirurgia recebidos: {}", surgeryDto);
        try {
            return addMedicalItem(patientId, "cirurgia", (patient) -> {
                if (patient.getCirurgias() == null) {
                    log.warn("Lista de cirurgias estava nula, inicializando nova lista.");
                    patient.setCirurgias(new ArrayList<>());
                }
                // Não gerar ID manualmente, deixar o banco gerar
                Surgery newSurgery = Surgery.builder()
                    .name(surgeryDto.getName())
                    .date(surgeryDto.getDate())
                    .location(surgeryDto.getLocation())
                    .notes(surgeryDto.getNotes())
                    .build();
                log.debug("Nova cirurgia criada: {}", newSurgery);
                patient.getCirurgias().add(newSurgery);
                log.debug("Cirurgia adicionada. Total de cirurgias: {}", patient.getCirurgias().size());
                return patient;
            });
        } catch (Exception e) {
            log.error("Erro ao adicionar cirurgia para paciente {}: {}", LogSanitizer.sanitizeId(patientId), e.getMessage(), e);
            throw e;
        }
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

    @PostMapping("/{patientId}/diagnoses")
    public ResponseEntity<ApiResponse<String>> addDiagnosis(
            @PathVariable int patientId,
            @Valid @RequestBody DiagnosisCreateDto diagnosisDto) {
        log.info("Iniciando adição de diagnóstico para paciente ID: {}", patientId);
        log.debug("Dados do diagnóstico recebidos: {}", diagnosisDto);
        
        return addMedicalItem(patientId, "diagnóstico", (patient) -> {
            log.debug("Diagnósticos atuais: {}", patient.getDiagnosticos().size());
            
            // ID será gerado automaticamente pelo banco
            Diagnosis newDiagnosis = new Diagnosis(0, diagnosisDto.getDescription(), diagnosisDto.getDate());
            patient.getDiagnosticos().add(newDiagnosis);
            
            log.debug("Diagnóstico adicionado. Total de diagnósticos: {}", patient.getDiagnosticos().size());
            return patient;
        });
    }

    @DeleteMapping("/{patientId}/diagnoses/{diagnosisId}")
    public ResponseEntity<ApiResponse<String>> deleteDiagnosis(
            @PathVariable int patientId,
            @PathVariable int diagnosisId) {
        log.info("Iniciando remoção de diagnóstico ID {} do paciente {}", diagnosisId, patientId);
        return removeMedicalItem(patientId, diagnosisId, "diagnóstico", patient -> {
            log.debug("Diagnósticos antes da remoção: {}", patient.getDiagnosticos().size());
            boolean removed = patient.getDiagnosticos().removeIf(diagnosis -> diagnosis.getId() == diagnosisId);
            log.debug("Diagnóstico removido: {}, Diagnósticos após remoção: {}", removed, patient.getDiagnosticos().size());
            return removed;
        });
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<List<Allergy>>> getAllergiesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getAlergias, "Alergias recuperadas com sucesso.");
    }

    @PostMapping("/{patientId}/allergies")
    public ResponseEntity<ApiResponse<String>> addAllergy(
            @PathVariable int patientId,
            @Valid @RequestBody Allergy allergyDto) {
        return addMedicalItem(patientId, "alergia", (patient) -> {
            int newId = generateNextId(patient.getAlergias(), Allergy::getId);
            allergyDto.setId(newId);
            patient.getAlergias().add(allergyDto);
            return patient;
        });
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<ApiResponse<String>> deleteAllergy(
            @PathVariable int patientId,
            @PathVariable int allergyId) {
        return removeMedicalItem(patientId, allergyId, "alergia", patient -> 
            patient.getAlergias().removeIf(allergy -> allergy.getId() == allergyId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> searchPatients(@RequestParam String name) {
        // TODO: Implementar patientService.findByNameContaining(name) para filtro no banco
        List<Patient> filteredPatients = patientService.findAll().stream()
                .filter(patient -> patient.getName() != null && patient.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        
        List<PatientResponseDto> response = filteredPatients.stream()
                .map(PatientResponseDto::fromEntity)
                .toList();
        
        return success("Pacientes encontrados.", response);
    }

    @PostMapping("/{patientId}/access-requests")
    public ResponseEntity<ApiResponse<String>> sendAccessRequest(
            @PathVariable int patientId, 
            @Valid @RequestBody AccessRequestDto request) {
        log.info("=== ENDPOINT ACCESS REQUEST CHAMADO ===");
        log.info("URL: POST /api/patients/{}/access-requests", patientId);
        log.info("PathVariable patientId: {}", patientId);
        log.info("RequestBody: {}", request);
        log.info("MedicoId do request: {}", request.getMedicoId());
        log.info("Status do request: {}", request.getStatus());
        
        try {
            patientService.sendAccessRequest(patientId, request.getMedicoId());
            log.info("✓ Solicitação processada com sucesso");
            return success("Requisição de acesso enviada com sucesso.", null);
        } catch (Exception e) {
            log.error("❌ Erro ao processar solicitação: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    // Endpoint de teste para verificar se a rota está funcionando
    @GetMapping("/{patientId}/access-requests/test")
    public ResponseEntity<String> testAccessRequestEndpoint(@PathVariable int patientId) {
        log.info("✓ Endpoint de teste chamado para paciente: {}", patientId);
        return ResponseEntity.ok("Endpoint funcionando para paciente " + patientId);
    }

    @GetMapping("/{patientId}/pending-requests")
    public ResponseEntity<ApiResponse<List<AccessRequestResponseDto>>> getPendingRequests(@PathVariable int patientId) {
        return executePatientOperation(patientId, patient -> {
            // Filter pending requests once to avoid duplicate iterations
            List<RequisicaoAcesso> pendingRequestsList = patient.getRequisicoesAcesso().stream()
                    .filter(req -> "PENDENTE".equals(req.getStatus()))
                    .collect(Collectors.toList());
            
            // Collect medic IDs from filtered list
            List<Integer> medicIds = pendingRequestsList.stream()
                    .map(RequisicaoAcesso::getMedicoId)
                    .distinct()
                    .collect(Collectors.toList());
            
            // Batch fetch all medics at once using optimized method
            Map<Integer, Medic> medicsMap = medicService.findByIds(medicIds);
            
            List<AccessRequestResponseDto> pendingRequests = pendingRequestsList.stream()
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
            
            return success("Requisições pendentes recuperadas.", pendingRequests);
        });
    }

    @PutMapping("/{patientId}/access-request/{medicId}")
    public ResponseEntity<ApiResponse<String>> updateAccessRequest(
            @PathVariable int patientId,
            @PathVariable int medicId,
            @RequestParam String action) {
        // Aceita tanto ACCEPTED/ACEITA quanto REJECTED/RECUSADA (case-insensitive)
        String actionUpper = action.trim().toUpperCase();
        String newStatus;
        String message;
        if ("ACCEPTED".equals(actionUpper) || "ACEITA".equals(actionUpper)) {
            newStatus = "ACEITA";
            message = "Acesso aceito com sucesso.";
            patientService.updateAccessRequestStatus(patientId, medicId, newStatus);
            Patient patient = findPatientOrThrow(patientId);
            authorizeSpecialist(patient, medicId, patientId);
            patientService.update(patientId, patient);
        } else if ("REJECTED".equals(actionUpper) || "RECUSADA".equals(actionUpper)) {
            newStatus = "RECUSADA";
            message = "Acesso rejeitado com sucesso.";
            patientService.updateAccessRequestStatus(patientId, medicId, newStatus);
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Ação inválida. Use ACCEPTED/ACEITA ou REJECTED/RECUSADA.", null));
        }
        return success(message, null);
    }

    @GetMapping("/{id}/authorized-doctors")
    public ResponseEntity<ApiResponse<List<AuthorizedDoctorDto>>> getAuthorizedDoctors(@PathVariable int id) {
        return executePatientOperation(id, patient -> {
            log.info("=== BUSCANDO MÉDICOS AUTORIZADOS ===");
            log.info("Paciente ID: {}", id);
            
            List<RequisicaoAcesso> accessRequests = patient.getRequisicoesAcesso();
            log.info("Total de requisições de acesso: {}", accessRequests != null ? accessRequests.size() : 0);
            
            if (accessRequests != null) {
                accessRequests.forEach(req -> {
                    log.info("Requisição: medicoId={}, status={}", req.getMedicoId(), req.getStatus());
                });
            }
            
            if (accessRequests == null || accessRequests.isEmpty()) {
                log.info("Nenhuma requisição de acesso encontrada");
                return success("Médicos autorizados recuperados com sucesso.", new ArrayList<>());
            }
            
            // Collect all authorized medic IDs to avoid N+1 queries
            List<Integer> authorizedMedicIds = accessRequests.stream()
                    .filter(req -> {
                        boolean isAccepted = "ACEITA".equals(req.getStatus());
                        log.info("Requisição medicoId={}, status={}, aceita={}", req.getMedicoId(), req.getStatus(), isAccepted);
                        return isAccepted;
                    })
                    .map(RequisicaoAcesso::getMedicoId)
                    .distinct()
                    .collect(Collectors.toList());
            
            log.info("Médicos autorizados (IDs): {}", authorizedMedicIds);
            
            // Batch fetch all authorized medics at once using optimized method
            Map<Integer, Medic> medicsMap = medicService.findByIds(authorizedMedicIds);
            
            // Build response using cached medics
            List<AuthorizedDoctorDto> authorizedDoctors = authorizedMedicIds.stream()
                    .map(medicsMap::get)
                    .filter(Objects::nonNull)
                    .map(medic -> {
                        log.info("Médico autorizado: ID={}, Nome={}", medic.getId(), medic.getName());
                        return new AuthorizedDoctorDto(medic.getId(), medic.getName(), medic.getSpecialty(), medic.getCrm());
                    })
                    .collect(Collectors.toList());
            
            log.info("Total de médicos autorizados retornados: {}", authorizedDoctors.size());
            return success("Médicos autorizados recuperados com sucesso.", authorizedDoctors);
        });
    }



    /**
     * Revoga o acesso de um médico ao prontuário do paciente.
     */
    @DeleteMapping("/{patientId}/doctors/{medicoId}/access")
    public ResponseEntity<ApiResponse<String>> revokeDoctorAccess(
            @PathVariable int patientId,
            @PathVariable int medicoId) {
        log.info("Revogando acesso do médico {} ao paciente {}", medicoId, patientId);
        try {
            patientService.revokeDoctorAccess(patientId, medicoId);
            return success("Acesso do médico revogado com sucesso.", null);
        } catch (Exception e) {
            log.error("Erro ao revogar acesso: {}", e.getMessage(), e);
            return error("Erro ao revogar acesso: " + e.getMessage());
        }
    }

    // Helper methods
    private Patient findPatientOrThrow(int patientId) {
        Patient patient = patientService.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Paciente não encontrado.");
        }
        return patient;
    }

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
        log.debug("Executando addMedicalItem para {} no paciente {}", itemType, LogSanitizer.sanitizeId(patientId));
        
        try {
            Patient patient = findPatientOrThrow(patientId);
            log.debug("Paciente encontrado para adição de {}: {}", itemType, patient.getName());
            
            Patient updatedPatient = addOperation.apply(patient);
            log.debug("Operação de adição de {} executada com sucesso", itemType);
            
            patientService.update(patientId, updatedPatient);
            log.info("{} adicionada com sucesso para paciente {}", itemType, LogSanitizer.sanitizeId(patientId));
            
            return success(itemType + " adicionada com sucesso.", null);
        } catch (Exception e) {
            log.error("Erro em addMedicalItem para {} no paciente {}: {}", itemType, LogSanitizer.sanitizeId(patientId), e.getMessage(), e);
            throw e;
        }
    }

    private ResponseEntity<ApiResponse<String>> removeMedicalItem(int patientId, int itemId, 
            String itemType, java.util.function.Function<Patient, Boolean> removeOperation) {
        Patient patient = findPatientOrThrow(patientId);
        boolean removed = removeOperation.apply(patient);
        
        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(itemType + " não encontrada."));
        }
        
        patientService.update(patientId, patient);
        return success(itemType + " removida com sucesso.", null);
    }

    private ResponseEntity<ApiResponse<String>> removeMedicalItemByName(int patientId, String itemName, 
            String itemType, java.util.function.Function<Patient, Boolean> removeOperation) {
        Patient patient = findPatientOrThrow(patientId);
        boolean removed = removeOperation.apply(patient);
        
        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(itemType + " não encontrado."));
        }
        
        patientService.update(patientId, patient);
        return success(itemType + " removido com sucesso.", null);
    }

    private <T> int generateNextId(java.util.List<T> items, java.util.function.ToIntFunction<T> idExtractor) {
        return items.stream().mapToInt(idExtractor).max().orElse(0) + 1;
    }

    private <T> ResponseEntity<ApiResponse<List<T>>> getPatientMedicalData(int id, java.util.function.Function<Patient, List<T>> dataExtractor, String successMessage) {
        return executePatientOperation(id, patient -> {
            List<T> data = dataExtractor.apply(patient);
            return success(successMessage, data);
        });
    }
}

