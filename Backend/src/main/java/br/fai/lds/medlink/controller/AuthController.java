package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.LoginRequest;
import br.fai.lds.medlink.domain.dataTransferObject.Medic.MedicResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
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

    // Serviço que faz a autenticação de usuários
    private final AuthenticationService authenticationService;


    @PostMapping("/login/medic")
    public ResponseEntity<?> loginMedic(@Valid @RequestBody LoginRequest request) {
        // Tenta autenticar o médico com email e senha
        var medic = authenticationService.authenticateMedic(request.getEmail(), request.getPassword());

        if (medic != null) {
            // Se encontrou, converte para DTO e retorna status 200 OK
            MedicResponseDto dto = MedicResponseDto.fromEntity(medic);
            return ResponseEntity.ok(dto);
        } else {
            // Se não encontrou, retorna erro 401 não autorizado com mensagem
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas");
        }
    }

    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@Valid @RequestBody LoginRequest request) {
        // Tenta autenticar o paciente com email e senha
        var patient = authenticationService.authenticatePatient(request.getEmail(), request.getPassword());

        if (patient != null) {
            // Se encontrou, converte para DTO e retorna status 200 OK
            PatientResponseDto dto = PatientResponseDto.fromEntity(patient);
            return ResponseEntity.ok(dto);
        } else {
            // Se não encontrou, retorna erro 401 não autorizado com mensagem
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas");
        }
    }
}
