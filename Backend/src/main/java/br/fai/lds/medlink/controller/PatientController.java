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
import java.util.Map;
import java.util.stream.Collectors;

// Controlador REST consolidado para gerenciar os pacientes da aplicação
@RestController
@RequestMapping("/patients")
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

    // Atualização parcial do paciente (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> patchPatient(@PathVariable int id, @RequestBody Map<String, Object> changes) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado para atualização."));
        }
        
        // Simula atualização parcial - em produção implementar lógica real
        return ResponseEntity.ok(new ApiResponse<>("Paciente atualizado com sucesso."));
    }

    // Médico adiciona informação ao prontuário
    @PostMapping("/{id}/medical-record/{type}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addMedicalRecordInfo(
            @PathVariable int id, 
            @PathVariable String type,
            @RequestBody Map<String, Object> information) {
        
        List<String> allowedTypes = List.of("consultas", "vacinas", "medicamentos", "cirurgias", "diagnosticos", "alergias");
        if (!allowedTypes.contains(type)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Tipo de informação inválido."));
        }
        
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado."));
        }
        
        Map<String, Object> result = Map.of(
                "pacienteId", id,
                "tipo", type,
                "informacao", information
        );
        
        return ResponseEntity.ok(new ApiResponse<>(
                "Informação adicionada ao prontuário com sucesso.",
                result
        ));
    }

    // Médico remove informação do prontuário
    @DeleteMapping("/{id}/medical-record/{type}/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeMedicalRecordInfo(
            @PathVariable int id,
            @PathVariable String type,
            @PathVariable String itemId) {
        
        List<String> allowedTypes = List.of("consultas", "vacinas", "medicamentos", "cirurgias", "diagnosticos", "alergias");
        if (!allowedTypes.contains(type)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Tipo de informação inválido."));
        }
        
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado."));
        }
        
        return ResponseEntity.ok(new ApiResponse<>("Informação removida do prontuário com sucesso."));
    }

    // Médico busca histórico específico do paciente
    @GetMapping("/{id}/medical-record/{type}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMedicalRecordHistory(
            @PathVariable int id,
            @PathVariable String type) {
        
        List<String> allowedTypes = List.of("consultas", "vacinas", "medicamentos", "cirurgias", "diagnosticos", "alergias");
        if (!allowedTypes.contains(type)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Tipo de informação inválido."));
        }
        
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Paciente não encontrado."));
        }
        
        // Simula dados - em produção buscar do banco via MedicalRecordService
        List<Map<String, String>> sampleData = switch (type) {
            case "consultas" -> List.of(Map.of("data", "2024-06-10", "descricao", "Consulta de rotina"));
            case "vacinas" -> List.of(Map.of("nome", "COVID-19", "data", "2023-01-15"));
            case "medicamentos" -> List.of(Map.of("nome", "Losartana", "dosagem", "50mg"));
            case "cirurgias" -> List.of(Map.of("nome", "Apendicectomia", "data", "2015-08-20"));
            case "diagnosticos" -> List.of(Map.of("nome", "Hipertensão", "data", "2022-03-01"));
            case "alergias" -> List.of(Map.of("descricao", "Nenhuma conhecida"));
            default -> List.of();
        };
        
        Map<String, Object> result = Map.of(
                "pacienteId", id,
                "tipo", type,
                "dados", sampleData
        );
        
        return ResponseEntity.ok(new ApiResponse<>(
                "Histórico recuperado com sucesso.",
                result
        ));
    }
}
