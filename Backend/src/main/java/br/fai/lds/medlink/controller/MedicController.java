package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.port.service.medic.MedicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medics")
public class MedicController {

    private final MedicService medicService;

    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    // Criar médico
    @PostMapping
    public ResponseEntity<MedicResponseDto> createMedic(@Valid @RequestBody MedicCreateDto dto) {
        Medic medic = dto.toEntity();
        int id = medicService.create(medic);
        medic.setId(id);
        return new ResponseEntity<>(MedicResponseDto.fromEntity(medic), HttpStatus.CREATED);
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<MedicResponseDto>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicResponseDto> dtos = medics.stream()
                .map(MedicResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<MedicResponseDto> getMedicById(@PathVariable int id) {
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MedicResponseDto.fromEntity(medic));
    }

    // Atualizar médico
    @PutMapping("/{id}")
    public ResponseEntity<MedicResponseDto> updateMedic(@PathVariable int id,
                                                        @Valid @RequestBody MedicUpdateDto dto) {
        Medic medic = medicService.findById(id);
        if (medic == null) {
            return ResponseEntity.notFound().build();
        }
        dto.updateEntity(medic);
        Medic updated = medicService.update(id, medic);
        return ResponseEntity.ok(MedicResponseDto.fromEntity(updated));
    }

    // Inativar médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
