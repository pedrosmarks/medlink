package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Controlador REST responsável por gerenciar os paientes da aplicação
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    // Injeção de dependências via construtor
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Endpoint para criar um novo paciente
    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponseDto>> createPatient(@Valid @RequestBody PatientCreateDto dto) {
        Patient patient = dto.toEntity();
        int id = patientService.create(patient);
        patient.setId(id);

        ApiResponse<PatientResponseDto> response = new ApiResponse<>(
                "Paciente criado com sucesso!",
                PatientResponseDto.fromEntity(patient)
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint para listar todos os pacientes cadastrados
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponseDto> dtos = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());

        ApiResponse<List<PatientResponseDto>> response = new ApiResponse<>(
                "Lista de pacientes carregada com sucesso.",
                dtos
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar um paciente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable int id) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado para o ID: " + id));
        }
        return ResponseEntity.ok(new ApiResponse<>(
                "Paciente encontrado.",
                PatientResponseDto.fromEntity(patient)
        ));
    }

    // Endpoint para atualizar dados de um paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable int id,
                                           @Valid @RequestBody PatientUpdateDto dto) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado para atualização."));
        }
        dto.updateEntity(patient);
        Patient updated = patientService.update(id, patient);

        return ResponseEntity.ok(new ApiResponse<>(
                "Paciente atualizado com sucesso!",
                PatientResponseDto.fromEntity(updated)
        ));
    }

    //Endpoint para "desativar" ou excluir um paciente pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivatePatient(@PathVariable int id) {
        boolean success = patientService.deactivate(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado para inativação."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Paciente inativado com sucesso."));
    }
}
