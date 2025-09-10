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
        Medic medic = dto.toEntity();
        int id = medicService.create(medic);
        medic.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Médico criado com sucesso!", MedicResponseDto.fromEntity(medic)));
    }

    // Endpoint para listar todos os médicos cadastrados
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicResponseDto>>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtos = medics.stream()
                .map(MedicResponseDto::fromEntity)
                .collect(Collectors.toList());
        return success("Lista de médicos recuperada com sucesso.", dtos);
    }

    // Endpoint para buscar um médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicResponseDto>> getMedicById(@PathVariable int id) {
        validateId(id);
        
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return notFound("Médico");
        }
        return success("Médico encontrado.", MedicResponseDto.fromEntity(medic));
    }

    // Endpoint para atualizar dados de um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicResponseDto>> updateMedic(@PathVariable int id,
                                         @Valid @RequestBody MedicUpdateDto dto) {
        validateId(id);
        
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return notFound("Médico");
        }

        dto.updateEntity(medic);
        Medic updated = medicService.update(id, medic);
        return success("Médico atualizado com sucesso!", MedicResponseDto.fromEntity(updated));
    }

    // Endpoint para "desativar" ou excluir um médico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateMedic(@PathVariable int id) {
        validateId(id);
        
        boolean deleteSuccess = medicService.delete(id);
        if (!deleteSuccess) {
            return notFound("Médico para exclusão");
        }
        return success("Médico removido com sucesso.");
    }

    // Listar pacientes vinculados ao médico
    @GetMapping("/{id}/patients")
    @CrossOrigin
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByMedic(@PathVariable("id") int medicId) {
        validateId(medicId);
        
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

        return success("Lista de pacientes do médico recuperada com sucesso.", dtos);
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
                return success("Login realizado com sucesso.", MedicResponseDto.fromEntity(medic));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("Credenciais inválidas."));
    }
}