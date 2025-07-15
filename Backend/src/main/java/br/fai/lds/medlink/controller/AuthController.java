package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.LoginDTO;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {

        var medic = authenticationService.authenticateMedic(loginDTO.getEmail(), loginDTO.getPassword());
        if (medic != null) {
            return ResponseEntity.ok(new LoginResponse(
                    medic.getId(),
                    medic.getName(),
                    "MEDIC"
            ));
        }

        var patient = authenticationService.authenticatePatient(loginDTO.getEmail(), loginDTO.getPassword());
        if (patient != null) {
            return ResponseEntity.ok(new LoginResponse(
                    patient.getId(),
                    patient.getName(),
                    "PATIENT"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Credenciais inválidas"));
    }

    private record LoginResponse(int id, String name, String profile) {
    }
}
