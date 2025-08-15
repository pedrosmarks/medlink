package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
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
@RequestMapping("/auth")
public class AuthController {

    // Serviço responsável pela lógica de autenticação e recuperação de senha.
    private final AuthenticationService authenticationService;

    //Realiza o login do usuário (médico ou paciente).
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody FrontendLoginDTO frontendLoginDTO) {
        String email = frontendLoginDTO.getUsuario();
        String password = frontendLoginDTO.getSenha();

        var medic = authenticationService.authenticateMedic(email, password);
        if (medic != null) {
            LoginResponseDTO response = new LoginResponseDTO(
                    medic.getId(),
                    medic.getName(),
                    "MEDIC"
            );
            return ResponseEntity.ok(new ApiResponse<>("Login realizado com sucesso.", response));
        }

        var patient = authenticationService.authenticatePatient(email, password);
        if (patient != null) {
            LoginResponseDTO response = new LoginResponseDTO(
                    patient.getId(),
                    patient.getName(),
                    "PATIENT"
            );
            return ResponseEntity.ok(new ApiResponse<>("Login realizado com sucesso.", response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("Credenciais inválidas."));
    }


    //Solicita o envio de um código de verificação para redefinição de senha.
    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        boolean success = authenticationService.sendVerificationCode(dto.getIdentifier());

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Usuário não encontrado com esse e-mail ou CPF."));
        }

        return ResponseEntity.ok(new ApiResponse<>("Código de verificação enviado."));
    }

    //Redefine a senha do usuário com base no código de verificação recebido.
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        boolean success = authenticationService.resetPassword(dto);

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Código inválido ou expirado."));
        }

        return ResponseEntity.ok(new ApiResponse<>("Senha redefinida com sucesso."));
    }
}