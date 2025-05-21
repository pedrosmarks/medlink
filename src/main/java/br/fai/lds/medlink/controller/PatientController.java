package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.PatientDto;
import br.fai.lds.medlink.mapper.PatientMapper;
import br.fai.lds.medlink.port.service.patient.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

//Gera automaticamente um construtor com os argumentos obrigatorios, no caso final
@RequiredArgsConstructor
//Retorno dos metodos e converte para JSON
@RestController
//Define os edpoint para todas as rotas
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    /**
     * GET: Chama o service para buscar todos os pacientes(findAll())
     * Converte cada entidade para um DTO
     */
    @GetMapping()
    public ResponseEntity<List<PatientDto>> getPatient() {

        List<Patient> patients = patientService.findAll();
        List<PatientDto> dtoList = patients.stream()
                .map(patientMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
    /**
     *GET: Chama o service para buscar um paciente especifico, definido pela rota "/{id}"
     *PathVariable: Pega o valor  da URL {id} e atribui a variavel int
     *findById é o responsável por chamar o service para fazer a busca do paciente
     *Se encontrar transforma em DTO e retorna um objeto  204,
     *Se não encontrar retorna 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getEntityById(@PathVariable final int id) {
        Patient entity = patientService.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        PatientDto dto = patientMapper.toDto(entity);
        return ResponseEntity.ok(dto);

    }
    /**
     *Responsável por criar um novo usuário paciente
     *Converte o DTO recebido atraves da requisicao em uma entidade
     */
    @PostMapping
    public ResponseEntity<PatientDto> createNew(@RequestBody PatientDto dto) {
        Patient entity = patientMapper.toEntity(dto);
        int id = patientService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.ok(dto);
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