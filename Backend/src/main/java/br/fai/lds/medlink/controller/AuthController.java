package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginResponseDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;

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
public class AuthController extends BaseController {

    // Serviço responsável pela lógica de autenticação e recuperação de senha.
    private final AuthenticationService authenticationService;

    //Realiza o login do usuário (médico ou paciente).
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        var medic = authenticationService.authenticateMedic(email, password);
        if (medic != null) {
            LoginResponseDTO response = new LoginResponseDTO(
                    medic.getId(),
                    medic.getName(),
                    "MEDIC"
            );
            return success("Login realizado com sucesso.", response);
        }

        var patient = authenticationService.authenticatePatient(email, password);
        if (patient != null) {
            LoginResponseDTO response = new LoginResponseDTO(
                    patient.getId(),
                    patient.getName(),
                    "PATIENT"
            );
            return success("Login realizado com sucesso.", response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("Email ou senha incorretos."));
    }


    //Solicita o envio de um código de verificação para redefinição de senha.
    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        boolean requestSuccess = authenticationService.sendVerificationCode(dto.getIdentifier());

        if (!requestSuccess) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Usuário não encontrado com esse e-mail ou CPF."));
        }

        return success("Código de verificação enviado.");
    }

    //Redefine a senha do usuário com base no código de verificação recebido.
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        boolean resetSuccess = authenticationService.resetPassword(dto);

        if (!resetSuccess) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Código inválido ou expirado."));
        }

        return success("Senha redefinida com sucesso.");
    }
}