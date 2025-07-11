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

    // Serviço de autenticação injetado automaticamente via construtor (Lombok)
    private final AuthenticationService authenticationService;

    // Classe interna para padronizar mensagens de erro em JSON
    record ErrorResponse(String message) {}

    @PostMapping("/login/medic")
    public ResponseEntity<?> loginMedic(@Valid @RequestBody LoginRequest request) {
        // Reutiliza metodo generico de login, passando função específica de autenticação e conversão para DTO de Medic
        return login(request.getEmail(), request.getPassword(),
                authenticationService::authenticateMedic,
                MedicResponseDto::fromEntity);
    }

    @PostMapping("/login/patient")
    public ResponseEntity<?> loginPatient(@Valid @RequestBody LoginRequest request) {
        return login(request.getEmail(), request.getPassword(),
                authenticationService::authenticatePatient,
                PatientResponseDto::fromEntity);
    }


    private <T, R> ResponseEntity<?> login(String email, String password,
                                           java.util.function.BiFunction<String, String, T> authFunction,
                                           java.util.function.Function<T, R> toDto) {
        // Tenta autenticar o usuário com as credenciais fornecidas
        T user = authFunction.apply(email, password);

        // Se autenticação bem-sucedida, retorna status 200 com o DTO do usuário
        if (user != null) {
            return ResponseEntity.ok(toDto.apply(user));
        }

        // Caso contrário, retorna status 401 (não autorizado) com mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciais inválidas"));
    }
}
