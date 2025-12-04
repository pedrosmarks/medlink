package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
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

    public JwtAuthController(AuthenticationService authenticationService,
                           JwtService jwtService,
                           UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<JwtToKenDto> authenticate(@RequestBody final LoginDTO loginDTO) {

        Object authenticatedUser = authenticationService.authenticate(
                loginDTO.getEmail(),
                loginDTO.getPassword());

        if(authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());

        String fullname;
        String role;
        String email;
        
        if (authenticatedUser instanceof Patient) {
            Patient patient = (Patient) authenticatedUser;
            fullname = patient.getName();
            role = "PATIENT";
            email = patient.getEmail();
        } else if (authenticatedUser instanceof Medic) {
            Medic medic = (Medic) authenticatedUser;
            fullname = medic.getName();
            role = "MEDIC";
            email = medic.getEmail();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String jwt = jwtService.generateToken(userDetails,
                fullname,
                role,
                email);

        if (jwt == null || jwt.isEmpty()){
            throw new InternalError("Token invalido");
        }

        System.out.println("token criado: " + jwt);
        JwtToKenDto jwtTokenDto = new JwtToKenDto(jwt);

        return ResponseEntity.ok(jwtTokenDto);
    }
}
