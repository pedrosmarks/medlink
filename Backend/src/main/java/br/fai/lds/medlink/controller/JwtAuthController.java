package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Login.LoginDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Jwt.JwtToKenDto;
import br.fai.lds.medlink.implementation.service.authentication.jwt.JwtService;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("jwt")
@RestController
@RequestMapping("/authenticate")
public class JwtAuthController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthController(AuthenticationService authenticationService, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<JwtToKenDto> authenticate(@RequestBody final LoginDTO loginDTO){
        System.out.println("=== JWT AUTH CONTROLLER ===");
        System.out.println("Login attempt for: " + loginDTO.getEmail());

        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            System.out.println("UserDetails loaded successfully");
            
            String jwt;
            // Tenta autenticar como paciente primeiro
            br.fai.lds.medlink.domain.Patient patient = authenticationService.authenticatePatient(loginDTO.getEmail(), loginDTO.getPassword());
            if (patient != null) {
                System.out.println("Authenticated as PATIENT: " + patient.getName());
                jwt = jwtService.generateTokens(userDetails, patient.getName(), "PACIENTE", 
                    loginDTO.getEmail(), String.valueOf(patient.getId()), String.valueOf(patient.getId()));
            } else {
                // Se não for paciente, tenta como médico
                br.fai.lds.medlink.domain.Medic medic = authenticationService.authenticateMedic(loginDTO.getEmail(), loginDTO.getPassword());
                if (medic != null) {
                    System.out.println("Authenticated as MEDIC: " + medic.getName());
                    jwt = jwtService.generateTokens(userDetails, medic.getName(), "MEDICO", 
                        loginDTO.getEmail(), String.valueOf(medic.getId()), String.valueOf(medic.getId()));
                } else {
                    System.out.println("Authentication FAILED for: " + loginDTO.getEmail());
                    throw new InternalError("Credenciais inválidas");
                }
            }
            
            if(jwt == null || jwt.isEmpty()){
                throw new InternalError("Token inválido");
            }
            
            System.out.println("Token JWT gerado com sucesso!");
            System.out.println("Token (primeiros 50 chars): " + jwt.substring(0, Math.min(50, jwt.length())) + "...");
            
            JwtToKenDto jwtTokenDto = new JwtToKenDto(jwt);
            
            return ResponseEntity.ok(jwtTokenDto);
        } catch (Exception e) {
            System.out.println("ERRO na autenticação: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
