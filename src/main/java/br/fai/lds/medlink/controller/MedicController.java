package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.dto.MedicDto;
import br.fai.lds.medlink.mapper.MedicMapper;
import br.fai.lds.medlink.port.service.medic.MedicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/api/medic")
public class MedicController {

    private final MedicService medicService;
    private final MedicMapper medicMapper;

    public MedicController(MedicService medicService, MedicMapper medicMapper) {

        this.medicService = medicService;
        this.medicMapper = medicMapper;
    }

    @GetMapping
    ResponseEntity<List<MedicDto>> getMedic(){

        List<Medic> medics = medicService.findAll();
        List<MedicDto> dtoList = medics.stream()
                .map(medicMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDto> getEntityById(@PathVariable final int id){
        Medic entity = medicService.findById(id);

        if (entity == null){

            return ResponseEntity.notFound().build();
        }
        MedicDto dto = medicMapper.toDto(entity);
        return ResponseEntity.ok(dto);

    }

    @PostMapping
    public ResponseEntity<MedicDto> createNew(@RequestBody final MedicDto dto) {
        Medic entity = medicMapper.toEntity(dto);
        int id = medicService.create(entity);
        dto.setId(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/remove")
    public ResponseEntity<Void> deleteMedic(@PathVariable int id) {
        boolean success = medicService.delete(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
