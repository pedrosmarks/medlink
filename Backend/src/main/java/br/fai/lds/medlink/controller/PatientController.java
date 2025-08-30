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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/patients")
@Slf4j
public class PatientController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicService medicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteResponseDto>>> getAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            List<PacienteResponseDto> response = patients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("Pacientes listados com sucesso.", response));
        } catch (Exception e) {
            log.error("Erro ao buscar todos os pacientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDto>> getPatientById(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            return ResponseEntity.ok(new ApiResponse<>("Paciente encontrado com sucesso.", PacienteResponseDto.fromEntity(patient)));
        } catch (Exception e) {
            log.error("Erro ao buscar paciente por ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Novo metodo UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDto>> updatePatient(@PathVariable int id, @Valid @RequestBody Patient patient) {
        try {
            Patient existingPatient = patientService.findById(id);
            if (existingPatient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }

            Patient updatedPatient = patientService.update(id, patient);
            return ResponseEntity.ok(new ApiResponse<>("Paciente atualizado com sucesso.", PacienteResponseDto.fromEntity(updatedPatient)));
        } catch (Exception e) {
            log.error("Erro ao atualizar paciente ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
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

    // Add vaccine to patient
    @PostMapping("/{patientId}/vaccines")
    public ResponseEntity<ApiResponse<String>> addVaccine(
            @PathVariable int patientId,
            @RequestBody VaccineCreateDto vaccineDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Generate new ID for vaccine
            int newVaccineId = patient.getVacinas().stream()
                .mapToInt(Vaccine::getId)
                .max()
                .orElse(0) + 1;
            
            Vaccine newVaccine = new Vaccine(newVaccineId, vaccineDto.getName(), vaccineDto.getDate());
            patient.getVacinas().add(newVaccine);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Vacina adicionada com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar vacina para paciente ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<List<Medication>>> getMedicationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getMedications, "Medicamentos recuperados com sucesso.");
    }

    @GetMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<List<Surgery>>> getSurgeriesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getCirurgias, "Cirurgias recuperadas com sucesso.");
    }

    // Add surgery to patient
    @PostMapping("/{patientId}/surgeries")
    public ResponseEntity<ApiResponse<String>> addSurgery(
            @PathVariable int patientId,
            @RequestBody SurgeryCreateDto surgeryDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Generate new ID for surgery
            int newSurgeryId = patient.getCirurgias().stream()
                .mapToInt(Surgery::getId)
                .max()
                .orElse(0) + 1;
            
            Surgery newSurgery = new Surgery(newSurgeryId, surgeryDto.getName(), 
                surgeryDto.getDate(), surgeryDto.getLocation(), surgeryDto.getNotes());
            patient.getCirurgias().add(newSurgery);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Cirurgia adicionada com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar cirurgia para paciente ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Delete surgery from patient
    @DeleteMapping("/{patientId}/surgeries/{surgeryId}")
    public ResponseEntity<ApiResponse<String>> deleteSurgery(
            @PathVariable int patientId,
            @PathVariable int surgeryId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            boolean removed = patient.getCirurgias().removeIf(surgery -> surgery.getId() == surgeryId);
            
            if (!removed) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Cirurgia não encontrada."));
            }
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Cirurgia removida com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao remover cirurgia ID {} do paciente ID {}: {}", surgeryId, patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<ApiResponse<List<Diagnosis>>> getDiagnosesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getDiagnosticos, "Diagnósticos recuperados com sucesso.");
    }

    // Add diagnosis to patient
    @PostMapping("/{patientId}/diagnoses")
    public ResponseEntity<ApiResponse<String>> addDiagnosis(
            @PathVariable int patientId,
            @RequestBody DiagnosisCreateDto diagnosisDto) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            // Generate new ID for diagnosis
            int newDiagnosisId = patient.getDiagnosticos().stream()
                .mapToInt(Diagnosis::getId)
                .max()
                .orElse(0) + 1;
            
            Diagnosis newDiagnosis = new Diagnosis(newDiagnosisId, diagnosisDto.getDescription(), diagnosisDto.getDate());
            patient.getDiagnosticos().add(newDiagnosis);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Diagnóstico adicionado com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao adicionar diagnóstico para paciente ID {}: {}", patientId, e.getMessage(), e);
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
            log.error("Erro ao remover diagnóstico ID {} do paciente ID {}: {}", diagnosisId, patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<List<Allergy>>> getAllergiesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getAlergias, "Alergias recuperadas com sucesso.");
    }

    // Endpoint para buscar pacientes por nome
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PacienteResponseDto>>> searchPatients(@RequestParam String name) {
        try {
            List<Patient> allPatients = patientService.findAll();
            List<Patient> filteredPatients = allPatients.stream()
                    .filter(patient -> patient.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            
            List<PacienteResponseDto> response = filteredPatients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>("Pacientes encontrados.", response));
        } catch (Exception e) {
            log.error("Erro ao buscar pacientes por nome '{}': {}", name, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Send access request to patient
    @PostMapping("/{patientId}/access-request")
    public ResponseEntity<ApiResponse<String>> sendAccessRequest(
            @PathVariable int patientId, 
            @RequestBody RequisicaoAcessoDto request) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            RequisicaoAcesso newRequest = new RequisicaoAcesso(request.getMedicoId(), "pendente");
            patient.getRequisicoesAcesso().add(newRequest);
            
            patientService.update(patientId, patient);
            
            return ResponseEntity.ok(new ApiResponse<>("Requisição de acesso enviada com sucesso."));
        } catch (Exception e) {
            log.error("Erro ao enviar requisição de acesso para paciente ID {}: {}", patientId, e.getMessage(), e);
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
            
            List<AccessRequestResponseDto> pendingRequests = patient.getRequisicoesAcesso().stream()
                    .filter(req -> "pendente".equals(req.getStatus()))
                    .map(req -> {
                        Medic medic = medicService.findById(req.getMedicoId());
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
            log.error("Erro ao buscar requisições pendentes do paciente ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Approve or reject access request
    @PutMapping("/{patientId}/access-request/{medicId}")
    public ResponseEntity<ApiResponse<String>> updateAccessRequest(
            @PathVariable int patientId,
            @PathVariable int medicId,
            @RequestParam String action) { // "approve" or "reject"
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            RequisicaoAcesso request = patient.getRequisicoesAcesso().stream()
                    .filter(req -> req.getMedicoId() == medicId && "pendente".equals(req.getStatus()))
                    .findFirst()
                    .orElse(null);
            
            if (request == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Requisição não encontrada."));
            }
            
            String newStatus = "approve".equals(action) ? "aprovado" : "rejeitado";
            request.setStatus(newStatus);
            
            // Se aprovado, vincular médico e paciente
            if ("approve".equals(action)) {
                log.info("Aprovando acesso do médico {} para paciente {}", medicId, patientId);
                
                // Adicionar médico à lista de especialistas autorizados do paciente
                if (patient.getEspecialistasAutorizados() == null) {
                    patient.setEspecialistasAutorizados(new ArrayList<>());
                    log.info("Lista de especialistas autorizados criada para paciente {}", patientId);
                }
                
                boolean medicAlreadyAuthorized = patient.getEspecialistasAutorizados().stream()
                        .anyMatch(esp -> esp.getMedicoId().intValue() == medicId);
                
                if (!medicAlreadyAuthorized) {
                    patient.getEspecialistasAutorizados().add(new EspecialistaAutorizado((long) medicId));
                    log.info("Médico {} adicionado à lista de especialistas do paciente {}", medicId, patientId);
                } else {
                    log.info("Médico {} já estava autorizado para paciente {}", medicId, patientId);
                }
                
                // Não alterar o médico principal, apenas adicionar como especialista autorizado
                // O paciente pode ter múltiplos médicos autorizados sem mudar o médico principal
                
                log.info("Paciente {} agora tem {} especialistas autorizados", patientId, patient.getEspecialistasAutorizados().size());
            }
            
            patientService.update(patientId, patient);
            log.info("Paciente {} atualizado no banco de dados", patientId);
            
            // Verificar se a atualização foi salva corretamente
            Patient updatedPatient = patientService.findById(patientId);
            if ("approve".equals(action) && updatedPatient.getEspecialistasAutorizados() != null) {
                log.info("Verificação: Paciente {} tem {} especialistas após atualização", 
                    patientId, updatedPatient.getEspecialistasAutorizados().size());
            }
            
            String message = "approve".equals(action) ? 
                "Acesso aprovado com sucesso. Médico vinculado ao paciente." : "Acesso rejeitado com sucesso.";
            
            return ResponseEntity.ok(new ApiResponse<>(message));
        } catch (Exception e) {
            log.error("Erro ao atualizar requisição de acesso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
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
            } else {
                debug.append("null\n");
            }
            
            return ResponseEntity.ok(debug.toString());
        } catch (Exception e) {
            return ResponseEntity.ok("Erro: " + e.getMessage());
        }
    }

    // Get authorized doctors for patient
    @GetMapping("/{patientId}/authorized-doctors")
    public ResponseEntity<ApiResponse<List<AuthorizedDoctorDto>>> getAuthorizedDoctors(@PathVariable int patientId) {
        try {
            Patient patient = patientService.findById(patientId);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            
            List<AuthorizedDoctorDto> authorizedDoctors = new ArrayList<>();
            
            if (patient.getEspecialistasAutorizados() != null) {
                for (EspecialistaAutorizado especialista : patient.getEspecialistasAutorizados()) {
                    Medic medic = medicService.findById(especialista.getMedicoId().intValue());
                    if (medic != null) {
                        authorizedDoctors.add(new AuthorizedDoctorDto(
                            medic.getId(),
                            medic.getName(),
                            medic.getSpecialty(),
                            medic.getCrm()
                        ));
                    }
                }
            }
            
            return ResponseEntity.ok(new ApiResponse<>("Médicos autorizados recuperados com sucesso.", authorizedDoctors));
        } catch (Exception e) {
            log.error("Erro ao buscar médicos autorizados do paciente ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
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

    // Metodo auxiliar para reduzir duplicação de código
    private <T> ResponseEntity<ApiResponse<List<T>>> getPatientMedicalData(int id, java.util.function.Function<Patient, List<T>> dataExtractor, String successMessage) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            List<T> data = dataExtractor.apply(patient);
            return ResponseEntity.ok(new ApiResponse<>(successMessage, data));
        } catch (Exception e) {
            log.error("Erro ao buscar dados médicos do paciente ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}