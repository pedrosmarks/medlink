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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Controlador REST consolidado para gerenciar os médicos da aplicação
@RestController
@RequestMapping("/medicos")
public class MedicController {

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
        Medic medic = dto.toEntity();
        int id = medicService.create(medic);
        medic.setId(id);

        ApiResponse<MedicResponseDto> response = new ApiResponse<>(
                "Médico criado com sucesso!",
                MedicResponseDto.fromEntity(medic)
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint para listar todos os médicos cadastrados
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicResponseDto>>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtos = medics.stream()
                .map(MedicResponseDto::fromEntity)
                .collect(Collectors.toList());

        ApiResponse<List<MedicResponseDto>> response = new ApiResponse<>(
                "Lista de médicos recuperada com sucesso.",
                dtos
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar um médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicById(@PathVariable int id) {
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Médico não encontrado para o ID: " + id));
        }

        ApiResponse<MedicResponseDto> response = new ApiResponse<>(
                "Médico encontrado.",
                MedicResponseDto.fromEntity(medic)
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint para atualizar dados de um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedic(@PathVariable int id,
                                         @Valid @RequestBody MedicUpdateDto dto) {
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
    }

    // Endpoint para "desativar" ou excluir um médico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Médico não encontrado para exclusão."));
        }
        return ResponseEntity.ok(new ApiResponse<>("Médico removido com sucesso."));
    }

    // Listar pacientes vinculados ao médico
    @GetMapping("/{id}/patients")
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByMedic(@PathVariable("id") int medicId) {
        List<Patient> patients = patientService.findByMedicId(medicId);
        List<PatientResponseDto> dtos = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());

        ApiResponse<List<PatientResponseDto>> response = new ApiResponse<>(
                "Lista de pacientes do médico recuperada com sucesso.",
                dtos
        );

        return ResponseEntity.ok(response);
    }

    // Endpoint para autenticação de médico (compatibilidade com frontend)
    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<MedicResponseDto>> authenticateMedic(
            @RequestParam String usuario,
            @RequestParam String senha) {

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
    }
}