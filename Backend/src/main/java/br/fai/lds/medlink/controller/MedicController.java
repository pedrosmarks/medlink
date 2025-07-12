package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/medic")
public class MedicController {

    private final MedicService medicService;

    //Retorna todos os médicos cadastrados.
    @GetMapping
    public ResponseEntity<List<MedicResponseDto>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtoList = medics
                .stream()
                .map(MedicResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    //Retorna os dados de um médico pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<MedicResponseDto> getMedicById(@PathVariable int id) {
        Medic entity = medicService.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        MedicResponseDto dto = MedicResponseDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }

    //Cria um novo médico
    @PostMapping
    public ResponseEntity<MedicResponseDto> createMedic(@Valid @RequestBody MedicCreateDto dto) {
        Medic entity = dto.toEntity();
        int id = medicService.create(entity);
        entity.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(MedicResponseDto.fromEntity(entity));
    }

    //Desativa um médico (soft delete) pelo ID.
    @PutMapping("/{id}/remove")
    public ResponseEntity<Void> deleteMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
