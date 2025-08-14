package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginResponseDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.FrontendLoginDTO;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Controlador responsável por gerenciar as operações de autenticação e recuperação de senha.

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@CrossOrigin
public class AuthController {

    // Serviço responsável pela lógica de autenticação e recuperação de senha.
    private final AuthenticationService authenticationService;

    //Realiza o login do usuário (médico ou paciente).
    @PostMapping
    public ResponseEntity<?> login(@RequestBody FrontendLoginDTO frontendLoginDTO) {
        String email = frontendLoginDTO.getUsuario();
        String password = frontendLoginDTO.getSenha();

        var medic = authenticationService.authenticateMedic(email, password);
        if (medic != null) {
            return ResponseEntity.ok(new LoginResponseDTO(
                    medic.getId(),
                    medic.getName(),
                    "MEDIC"
            ));
        }

        var patient = authenticationService.authenticatePatient(email, password);
        if (patient != null) {
            return ResponseEntity.ok(new LoginResponseDTO(
                    patient.getId(),
                    patient.getName(),
                    "PATIENT"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
    }


    //Solicita o envio de um código de verificação para redefinição de senha.
    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        boolean success = authenticationService.sendVerificationCode(dto.getIdentifier());

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado com esse e-mail ou CPF.");
        }

        return ResponseEntity.ok("Código de verificação enviado.");
    }

    //Redefine a senha do usuário com base no código de verificação recebido.
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        boolean success = authenticationService.resetPassword(dto);

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Código inválido ou expirado.");
        }

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}