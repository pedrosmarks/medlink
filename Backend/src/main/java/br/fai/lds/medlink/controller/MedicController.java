package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import br.fai.lds.medlink.util.LogSanitizer;

// Controlador REST consolidado para gerenciar os médicos da aplicação
@Slf4j
@RestController
@RequestMapping("/api/medic")
public class MedicController extends BaseController {

    private final MedicService medicService;
    private final PatientService patientService;

    // Injeção de dependências via construtor
    public MedicController(MedicService medicService, PatientService patientService) {
        this.medicService = medicService;
        this.patientService = patientService;
    }

    // Endpoint para criar um novo médico
    @PostMapping
    public ResponseEntity<ApiResponse<MedicResponseDto>> createMedic(@Valid @RequestBody MedicCreateDto dto) {
        try {
            Medic medic = dto.toEntity();
            int id = medicService.create(medic);
            medic.setId(id);

            ApiResponse<MedicResponseDto> response = new ApiResponse<>(
                    "Médico criado com sucesso!",
                    MedicResponseDto.fromEntity(medic)
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

    // Endpoint para listar todos os médicos cadastrados
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicResponseDto>>> getAllMedics() {
        try {
            List<Medic> medics = medicService.findAll();
            List<MedicResponseDto> dtos = medics.stream()
                    .map(MedicResponseDto::fromEntity)
                    .collect(Collectors.toList());

            ApiResponse<List<MedicResponseDto>> response = new ApiResponse<>(
                    "Lista de médicos recuperada com sucesso.",
                    dtos
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    // Endpoint para buscar um médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicResponseDto>> getMedicById(@PathVariable int id) {
        ResponseEntity<ApiResponse<MedicResponseDto>> validationError = validateId(id);
        if (validationError != null) return validationError;

        return executeWithErrorHandling(() -> {
            Medic medic = medicService.findById(id);
            if (medic == null) {
                return notFound("Médico");
            }
            return success("Médico encontrado.", MedicResponseDto.fromEntity(medic));
        }, "getMedicById");
    }

    // Endpoint para atualizar dados de um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedic(@PathVariable int id,
                                         @Valid @RequestBody MedicUpdateDto dto) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("ID deve ser maior que zero."));
            }
            Medic medic = medicService.findById(id);
            if (medic == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("Médico não encontrado para atualização."));
            }

            dto.updateEntity(medic);
            Medic updated = medicService.update(id, medic);

            ApiResponse<MedicResponseDto> response = new ApiResponse<>(
                    "Médico atualizado com sucesso!",
                    MedicResponseDto.fromEntity(updated)
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

    // Endpoint para "desativar" ou excluir um médico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateMedic(@PathVariable int id) {
        ResponseEntity<ApiResponse<Void>> validationError = validateId(id);
        if (validationError != null) return validationError;

        return executeWithErrorHandling(() -> {
            boolean deleteSuccess = medicService.delete(id);
            if (!deleteSuccess) {
                return notFound("Médico para exclusão");
            }
            return success("Médico removido com sucesso.");
        }, "deactivateMedic");
    }

    // Listar pacientes vinculados ao médico
    @GetMapping("/{id}/patients")
    @CrossOrigin
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByMedic(@PathVariable("id") int medicId) {
        ResponseEntity<ApiResponse<List<PatientResponseDto>>> validationError = validateId(medicId);
        if (validationError != null) return validationError;

        return executeWithErrorHandling(() -> {
            log.debug("Buscando pacientes para médico ID: {}", LogSanitizer.sanitizeId(medicId));
            
            List<Patient> patients = patientService.findByMedicId(medicId);
            log.debug("Encontrados {} pacientes para médico {}", patients.size(), LogSanitizer.sanitizeId(medicId));
            
            for (Patient p : patients) {
                log.debug("Paciente: {} (ID: {}), medicId: {}", LogSanitizer.sanitizeAndLimit(p.getName(), 30), LogSanitizer.sanitizeId(p.getId()), LogSanitizer.sanitizeId(p.getMedicId()));
                if (p.getEspecialistasAutorizados() != null) {
                    log.debug("  Especialistas autorizados: {}", p.getEspecialistasAutorizados().size());
                }
            }
            
            List<PatientResponseDto> dtos = patients.stream()
                    .map(PatientResponseDto::fromEntity)
                    .collect(Collectors.toList());

            ApiResponse<List<PatientResponseDto>> response = new ApiResponse<>(
                    "Lista de pacientes do médico recuperada com sucesso.",
                    dtos
            );

            return success("Lista de pacientes do médico recuperada com sucesso.", dtos);
        }, "getPatientsByMedic");
    }

    // Endpoint para autenticação de médico (compatibilidade com frontend)
    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<MedicResponseDto>> authenticateMedic(
            @RequestParam String usuario,
            @RequestParam String senha) {
        try {
            // Simulação de autenticação - em produção usar Spring Security
            if ("admin".equals(usuario) && "123".equals(senha)) {
                Medic medic = medicService.findById(1); // Busca médico padrão
                if (medic != null) {
                    ApiResponse<MedicResponseDto> response = new ApiResponse<>(
                            "Login realizado com sucesso.",
                            MedicResponseDto.fromEntity(medic)
                    );
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Credenciais inválidas."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }
}