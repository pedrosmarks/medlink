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

    private final AuthenticationService authenticationService;

    record ErrorResponse(String message) {}

    @PostMapping("/login/medic")
    public ResponseEntity<?> loginMedic(@Valid @RequestBody LoginRequest request) {
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
        T user = authFunction.apply(email, password);
        if (user != null) {
            return ResponseEntity.ok(toDto.apply(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciais inválidas"));
    }
}
