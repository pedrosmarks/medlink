package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PacienteResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteResponseDto>>> getAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            List<PacienteResponseDto> response = patients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("Pacientes listados com sucesso.", response));
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Novo método UPDATE
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoints para buscar dados médicos específicos do paciente
    
    @GetMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<List<Consulta>>> getConsultationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getConsultas, "Consultas recuperadas com sucesso.");
    }

    @GetMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<List<Vacina>>> getVaccinesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getVacinas, "Vacinas recuperadas com sucesso.");
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<List<Medicamento>>> getMedicationsByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getMedicamentos, "Medicamentos recuperados com sucesso.");
    }

    @GetMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<List<Cirurgia>>> getSurgeriesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getCirurgias, "Cirurgias recuperadas com sucesso.");
    }

    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<ApiResponse<List<Diagnostico>>> getDiagnosesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getDiagnosticos, "Diagnósticos recuperados com sucesso.");
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<List<Alergia>>> getAllergiesByPatient(@PathVariable int id) {
        return getPatientMedicalData(id, Patient::getAlergias, "Alergias recuperadas com sucesso.");
    }

    // Método auxiliar para reduzir duplicação de código
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}