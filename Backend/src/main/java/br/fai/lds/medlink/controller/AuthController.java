package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginResponseDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;

import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável por gerenciar as operações de autenticação e recuperação de senha.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final AuthenticationService authenticationService;

    /**
     * Realiza o login do usuário (médico ou paciente).
     * @param loginDTO dados de login contendo email e senha
     * @return resposta com dados do usuário autenticado ou erro de autenticação
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // Tenta autenticar como PACIENTE primeiro
        var patient = authenticationService.authenticatePatient(email, password);
        if (patient != null) {
            return success("Login realizado com sucesso.", new LoginResponseDTO(
                    patient.getId(), patient.getName(), "PATIENT"));
        }

        // Se não for paciente, tenta autenticar como MÉDICO
        var medic = authenticationService.authenticateMedic(email, password);
        if (medic != null) {
            System.out.println("=== LOGIN CONTROLLER - MÉDICO ===");
            System.out.println("Médico autenticado: ID=" + medic.getId() + ", Nome=" + medic.getName());
            System.out.println("Retornando LoginResponseDTO com ID: " + medic.getId());
            
            return success("Login realizado com sucesso.", new LoginResponseDTO(
                    medic.getId(), medic.getName(), "MEDIC"));
        }


        return unauthorized("Email ou senha incorretos.");
    }


    /**
     * Solicita o envio de um código de verificação para redefinição de senha.
     * @param dto dados contendo identificador (email ou CPF) do usuário
     * @return resposta de sucesso ou erro se usuário não encontrado
     */
    @PostMapping("/request-password-reset")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        boolean requestSuccess = authenticationService.sendVerificationCode(dto.getIdentifier());

        if (!requestSuccess) {
            return notFoundCustom("Usuário não encontrado com esse e-mail ou CPF.");
        }

        return success("Código de verificação enviado.");
    }

    /**
     * Redefine a senha do usuário com base no código de verificação recebido.
     * @param dto dados contendo código de verificação e nova senha
     * @return resposta de sucesso ou erro se código inválido
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        boolean resetSuccess = authenticationService.resetPassword(dto);

        if (!resetSuccess) {
            return badRequest("Código inválido ou expirado.");
        }

        return success("Senha redefinida com sucesso.");
    }
}