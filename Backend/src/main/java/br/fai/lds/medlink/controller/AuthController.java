package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.LoginRequest;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.PatientDto;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login/medic")
    public ResponseEntity<?> loginMedic (@RequestBody LoginRequest request){
        Medic medic = authenticationService.authenticateMedic(request.getEmail(), request.getPassword());
        if( medic != null){
            MedicDto dto = MedicDto.fromEntity(medic);
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest request) {
        Patient patient = authenticationService.authenticatePatient(request.getEmail(), request.getPassword());
        if (patient != null) {
            PatientDto dto = PatientDto.fromEntity(patient);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
}
