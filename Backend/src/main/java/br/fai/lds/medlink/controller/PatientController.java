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
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/api/pacientes")
    public ResponseEntity<List<PacienteResponseDto>> getAllPacientes() {
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

    @GetMapping("/api/pacientes/{id}")
    public ResponseEntity<PacienteResponseDto> getPacienteById(@PathVariable int id) {
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
    @PutMapping("/api/pacientes/{id}")
    public ResponseEntity<PacienteResponseDto> updatePaciente(@PathVariable int id, @RequestBody Patient patient) {
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

    // Endpoint para autenticação de paciente (compatibilidade com frontend)
    @GetMapping("/api/pacientes/auth")
    public ResponseEntity<ApiResponse<PacienteResponseDto>> authenticatePatient(
            @RequestParam String usuario,
            @RequestParam String senha) {

        // Busca paciente por email
        Patient patient = patientService.findByEmail(usuario);
        
        if (patient != null && senha.equals(patient.getPassword())) {
            ApiResponse<PacienteResponseDto> response = new ApiResponse<>(
                    "Login realizado com sucesso.",
                    PacienteResponseDto.fromEntity(patient)
            );
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("Credenciais inválidas."));
    }

    // Endpoints para buscar dados médicos específicos do paciente
    
    @GetMapping("/api/pacientes/{id}/consultas")
    public ResponseEntity<ApiResponse<List<Consulta>>> getConsultasByPatient(@PathVariable int id) {
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

    @GetMapping("/api/pacientes/{id}/vacinas")
    public ResponseEntity<ApiResponse<List<Vacina>>> getVacinasByPatient(@PathVariable int id) {
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

    @GetMapping("/api/pacientes/{id}/medicamentos")
    public ResponseEntity<ApiResponse<List<Medicamento>>> getMedicamentosByPatient(@PathVariable int id) {
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

    @GetMapping("/api/pacientes/{id}/cirurgias")
    public ResponseEntity<ApiResponse<List<Cirurgia>>> getCirurgiasByPatient(@PathVariable int id) {
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

    @GetMapping("/api/pacientes/{id}/diagnosticos")
    public ResponseEntity<ApiResponse<List<Diagnostico>>> getDiagnosticosByPatient(@PathVariable int id) {
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

    @GetMapping("/api/pacientes/{id}/alergias")
    public ResponseEntity<ApiResponse<List<Alergia>>> getAlergiasByPatient(@PathVariable int id) {
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