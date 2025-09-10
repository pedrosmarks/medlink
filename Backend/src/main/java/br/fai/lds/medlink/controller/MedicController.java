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

/**
 * Controlador REST para gerenciar os médicos da aplicação.
 */
@Slf4j
@RestController
@RequestMapping("/api/medic")
public class MedicController extends BaseController {

    private final MedicService medicService;
    private final PatientService patientService;

    public MedicController(MedicService medicService, PatientService patientService) {
        this.medicService = medicService;
        this.patientService = patientService;
    }

    /**
     * Cria um novo médico.
     * @param dto dados do médico a ser criado
     * @return resposta com dados do médico criado
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MedicResponseDto>> createMedic(@Valid @RequestBody MedicCreateDto dto) {
        Medic medic = dto.toEntity();
        int id = medicService.create(medic);
        medic.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Médico criado com sucesso!", MedicResponseDto.fromEntity(medic)));
    }

    /**
     * Lista todos os médicos cadastrados.
     * @return lista de médicos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicResponseDto>>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtos = medics.stream()
                .map(MedicResponseDto::fromEntity)
                .collect(Collectors.toList());
        return success("Lista de médicos recuperada com sucesso.", dtos);
    }

    /**
     * Busca um médico pelo ID.
     * @param id identificador do médico
     * @return dados do médico encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicResponseDto>> getMedicById(@PathVariable int id) {
        validateId(id);
        
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return notFound("Médico");
        }
        return success("Médico encontrado.", MedicResponseDto.fromEntity(medic));
    }

    /**
     * Atualiza dados de um médico existente.
     * @param id identificador do médico
     * @param dto novos dados do médico
     * @return dados do médico atualizado
     */
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

    /**
     * Remove um médico pelo ID.
     * @param id identificador do médico
     * @return confirmação da remoção
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateMedic(@PathVariable int id) {
        validateId(id);
        
        boolean deleteSuccess = medicService.delete(id);
        if (!deleteSuccess) {
            return notFound("Médico para exclusão");
        }
        return success("Médico removido com sucesso.");
    }

    /**
     * Lista pacientes vinculados ao médico.
     * @param medicId identificador do médico
     * @return lista de pacientes do médico
     */
    @GetMapping("/{id}/patients")
    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, 
                methods = {RequestMethod.GET})
    public ResponseEntity<ApiResponse<List<PatientResponseDto>>> getPatientsByMedic(@PathVariable("id") int medicId) {
        validateId(medicId);
        
        log.debug("Buscando pacientes para médico ID: {}", LogSanitizer.sanitizeId(medicId));
        
        List<Patient> patients = patientService.findByMedicId(medicId);
        log.debug("Encontrados {} pacientes para médico {}", patients.size(), LogSanitizer.sanitizeId(medicId));
        
        List<PatientResponseDto> dtos = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());

        return success("Lista de pacientes do médico recuperada com sucesso.", dtos);
    }

    /**
     * Autentica médico (compatibilidade com frontend).
     * @param usuario nome de usuário
     * @param senha senha do usuário
     * @return dados do médico autenticado
     */
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