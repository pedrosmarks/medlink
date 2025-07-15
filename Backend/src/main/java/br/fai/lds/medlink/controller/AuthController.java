package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        boolean success = authenticationService.sendVerificationCode(dto.getIdentifier());

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado com esse e-mail ou CPF."));
        }

        return ResponseEntity.ok(Map.of("message", "Código de verificação enviado."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        boolean success = authenticationService.resetPassword(
                dto.getIdentifier(),
                dto.getVerificationCode(),
                dto.getNewPassword()
        );

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Código inválido ou expirado."));
        }

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
    }

    private record LoginResponse(int id, String name, String profile) {
    }
}
