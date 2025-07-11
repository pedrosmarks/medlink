package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/medic")
    public ResponseEntity<?> loginMedic(@Valid @RequestBody LoginDTO loginDTO) {
        var medic = authenticationService.authenticateMedic(loginDTO.getEmail(), loginDTO.getPassword());

        if (medic != null) {
            MedicResponseDto dto = MedicResponseDto.fromEntity(medic);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas");
        }
    }

    @PostMapping("/patient")
    public ResponseEntity<?> loginPatient(@Valid @RequestBody LoginDTO loginDTO) {
        var patient = authenticationService.authenticatePatient(loginDTO.getEmail(), loginDTO.getPassword());

        if (patient != null) {
            PatientResponseDto dto = PatientResponseDto.fromEntity(patient);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas");
        }
    }
}
