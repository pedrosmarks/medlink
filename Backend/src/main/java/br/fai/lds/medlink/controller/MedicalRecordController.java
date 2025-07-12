package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    // Retorna a lista de todos os prontuários médicos.
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> getAll() {
        List<MedicalRecord> entities = medicalRecordService.findAll();
        List<MedicalRecordResponseDto> dtos = entities.stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Retorna um prontuário médico pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getById(@PathVariable int id) {
        MedicalRecord entity = medicalRecordService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        MedicalRecordResponseDto dto = MedicalRecordResponseDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }

    // Cria um novo prontuário médico.
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordCreateDto dto) {
        MedicalRecord entity = dto.toEntity();
        int id = medicalRecordService.create(entity);
        entity.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(MedicalRecordResponseDto.fromEntity(entity));
    }

    // Atualiza um prontuário médico existente.
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(
            @PathVariable int id,
            @Valid @RequestBody MedicalRecordUpdateDto dto
    ) {
        MedicalRecord entity = dto.toEntity();
        MedicalRecord updated = medicalRecordService.update(id, entity);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        MedicalRecordResponseDto updatedDto = MedicalRecordResponseDto.fromEntity(updated);
        return ResponseEntity.ok(updatedDto);
    }

    // Desativa (deleta) um prontuário médico pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
