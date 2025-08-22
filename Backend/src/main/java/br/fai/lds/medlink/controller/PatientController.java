package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PacienteResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
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
    public ResponseEntity<List<PacienteResponseDto>> getAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            List<PacienteResponseDto> response = patients.stream()
                    .map(PacienteResponseDto::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDto> getPatientById(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PacienteResponseDto.fromEntity(patient));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Novo método UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDto> updatePatient(@PathVariable int id, @RequestBody Patient patient) {
        try {
            Patient existingPatient = patientService.findById(id);
            if (existingPatient == null) {
                return ResponseEntity.notFound().build();
            }

            Patient updatedPatient = patientService.update(id, patient);
            return ResponseEntity.ok(PacienteResponseDto.fromEntity(updatedPatient));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoints para buscar dados médicos específicos do paciente
    
    @GetMapping("/{id}/consultations")
    public ResponseEntity<ApiResponse<List<Consulta>>> getConsultationsByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Consulta>> response = new ApiResponse<>(
                    "Consultas recuperadas com sucesso.",
                    patient.getConsultas()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/vaccines")
    public ResponseEntity<ApiResponse<List<Vacina>>> getVaccinesByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Vacina>> response = new ApiResponse<>(
                    "Vacinas recuperadas com sucesso.",
                    patient.getVacinas()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<ApiResponse<List<Medicamento>>> getMedicationsByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Medicamento>> response = new ApiResponse<>(
                    "Medicamentos recuperados com sucesso.",
                    patient.getMedicamentos()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/surgeries")
    public ResponseEntity<ApiResponse<List<Cirurgia>>> getSurgeriesByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Cirurgia>> response = new ApiResponse<>(
                    "Cirurgias recuperadas com sucesso.",
                    patient.getCirurgias()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<ApiResponse<List<Diagnostico>>> getDiagnosesByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Diagnostico>> response = new ApiResponse<>(
                    "Diagnósticos recuperados com sucesso.",
                    patient.getDiagnosticos()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<ApiResponse<List<Alergia>>> getAllergiesByPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Paciente não encontrado."));
            }
            ApiResponse<List<Alergia>> response = new ApiResponse<>(
                    "Alergias recuperadas com sucesso.",
                    patient.getAlergias()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}