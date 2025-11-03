package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginResponseDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;

import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável por gerenciar as operações de autenticação e recuperação de senha.
 */
@Profile("basic")
@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
public class AuthController extends BaseController {

    private final AuthenticationService authenticationService;

    /**
     * Realiza o login do usuário (médico ou paciente).
     * @param loginDTO dados de login contendo email e senha
     * @return resposta com dados do usuário autenticado ou erro de autenticação
     */
    @PostMapping()
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        String userType = loginDTO.getUserType();

        if ("PATIENT".equals(userType)) {
            var patient = authenticationService.authenticatePatient(email, password);
            if (patient != null) {
                return success("Login realizado com sucesso.", new LoginResponseDTO(
                        patient.getId(), patient.getName(), "PATIENT"));
            }
        } else if ("MEDIC".equals(userType)) {
            var medic = authenticationService.authenticateMedic(email, password);
            if (medic != null) {
                return success("Login realizado com sucesso.", new LoginResponseDTO(
                        medic.getId(), medic.getName(), "MEDIC"));
            }
        } else {
            return badRequest("Tipo de usuário inválido. Use 'MEDIC' ou 'PATIENT'.");
        }

        return unauthorized("Email ou senha incorretos.");
    }
}