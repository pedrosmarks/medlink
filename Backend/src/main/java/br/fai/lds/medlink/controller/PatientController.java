package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

//Gera automaticamente um construtor com os argumentos obrigatorios, no caso final
@RequiredArgsConstructor
//Retorno dos metodos e converte para JSON
@RestController
//Define os edpoint para todas as rotas
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;
    private AuthenticationService authenticationService;

    /**
     * GET: Chama o service para buscar todos os pacientes(findAll())
     * Converte cada entidade para um DTO
     */
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getPatient() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponseDto> dtoList = patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
    /**
     *GET: Chama o service para buscar um paciente especifico, definido pela rota "/{id}"
     *PathVariable: Pega o valor  da URL {id} e atribui a variavel int
     *findById é o responsável por chamar o service para fazer a busca do paciente
     *Se encontrar transforma em DTO e retorna um objeto  200,
     *Se não encontrar retorna 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getEntityById(@PathVariable final int id) {
        Patient entity = patientService.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        PatientResponseDto dto = PatientResponseDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    /**
     *Responsável por criar um novo usuário paciente
     *Converte o DTO recebido atraves da requisicao em uma entidade
     */
    @PostMapping
    public ResponseEntity<PatientCreateDto> createNew(@RequestBody PatientCreateDto dto) {
        Patient entity = dto.toEntity();
        int id = patientService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }
    /**
     remove
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean result = patientService.deactivate(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}