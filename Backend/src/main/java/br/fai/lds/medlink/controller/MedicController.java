package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import br.fai.lds.medlink.port.service.medic.MedicService;
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
@RequestMapping("/api/medic")
public class MedicController {


    private final MedicService medicService;
    private final AuthenticationService authenticationService;


    /**
     * GET: Chama o service para buscar todos os médicos(findAll())
     * Converte cada entidade para um DTO
     */
    @GetMapping
    public ResponseEntity<List<MedicDto>> getAllMedics() {
        List<Medic> medics = medicService.findAll();
        List<MedicDto> dtoList = medics
                .stream()
                .map(MedicDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
    /**
     *GET: Chama o service para buscar um medico especifico, definido pela rota "/{id}"
     *PathVariable: Pega o valor  da URL {id} e atribui a variavel int
     *findById é o responsável por chamar o service para fazer a busca do medico
     *Se encontrar transforma em DTO e retorna um objeto  200,
     *Se não encontrar retorna 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicDto> getMedicById(@PathVariable int id) {
        Medic entity = medicService.findById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        MedicDto dto = MedicDto.fromEntity(entity);
        return ResponseEntity.ok(dto);
    }
    /**
     *Responsável por criar um novo usuário médico
     *Converte o DTO recebido atraves da requisicao em uma entidade
     */
    @PostMapping
    public ResponseEntity<MedicDto> createMedic(@RequestBody MedicDto dto) {
        Medic entity = dto.toEntity();
        int id = medicService.create(entity);
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
    @PutMapping("/{id}/remove")
    public ResponseEntity<Void> deactivate(@PathVariable int id) {
        boolean success = medicService.delete(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
