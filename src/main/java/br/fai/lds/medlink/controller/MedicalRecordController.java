package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.dto.MedicalRecordDto;
import br.fai.lds.medlink.mapper.MedicalRecordMapper;
import br.fai.lds.medlink.port.service.medicalrecord.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordMapper medicalRecordMapper;


    // GET: Buscar todos os prontuários
    @GetMapping
    public ResponseEntity<List<MedicalRecordDto>> getAll() {
        List<MedicalRecord> records = medicalRecordService.findAll();
        List<MedicalRecordDto> dtoList = records.stream()
                .map(medicalRecordMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> getById(@PathVariable int id) {
        MedicalRecord entity = medicalRecordService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        MedicalRecordDto dto = medicalRecordMapper.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDto> create(@RequestBody MedicalRecordDto dto) {
        MedicalRecord entity = medicalRecordMapper.toEntity(dto);
        int id = medicalRecordService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDto> update(@PathVariable int id, @RequestBody MedicalRecordDto dto) {
        MedicalRecord entity = medicalRecordMapper.toEntity(dto);
        MedicalRecord updated = medicalRecordService.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        MedicalRecordDto updatedDto = medicalRecordMapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = medicalRecordService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

