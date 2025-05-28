package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecordDto;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
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
@RequestMapping("/api/medical-record")

public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;


    /**
     * GET: Chama o service para buscar todos os prontuarios(findAll())
     * Converte cada entidade para um DTO
     */
    @GetMapping
    public ResponseEntity<List<MedicalRecordDto>> getAll() {
        List<MedicalRecord> entities = medicalRecordService.findAll();

        List<MedicalRecordDto> dto = entities
                .stream()
                .map(MedicalRecordDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dto);
    }
    /**
     *GET: Chama o service para buscar um prontuario especifico, definido pela rota "/{id}"
     *PathVariable: Pega o valor  da URL {id} e atribui a variavel int
     *findById é o responsável por chamar o service para fazer a busca do prontuario
     *Se encontrar transforma em DTO e retorna um objeto 201,
     *Se não encontrar retorn 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getById(@PathVariable int id) {
        MedicalRecord entity = medicalRecordService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        
        MedicalRecordDto dto = MedicalRecordDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    /**
    *Responsável por criar um novo prontuario
    *Converte o DTO recebido atraves da requisicao em uma entidade
    */
    @PostMapping
    public ResponseEntity<MedicalRecordDto> create(@RequestBody MedicalRecordDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }
    /**
     * Atualiza um prontuario médico já existente
     * Converte o DTO recebido em uma entidade de dominio
     * Salva no banco,
     * Se retornar null é que o Id passado pela rota não existe
     * Se retornar 200 é que o prontuario foi encontrado e atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> update(@PathVariable int id, @RequestBody MedicalRecordDto dto) {
        MedicalRecord entity = dto.toEntity();
        MedicalRecord updated = medicalRecordService.update(id, entity);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        MedicalRecordDto updatedDto = MedicalRecordDto.fromEntity(updated);
        return ResponseEntity.ok(updatedDto);

    }

    /**
     * Deleta um prontuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

