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

        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            
            final String jwt = jwtService.generateTokens(userDetails, "User Name", "USER", loginDTO.getEmail());
            
            if(jwt == null || jwt.isEmpty()){
                throw new InternalError("Token inválido");
            }
            
            System.out.println("Token criado: " + jwt);
            
            JwtToKenDto jwtTokenDto = new JwtToKenDto(jwt);
            
            return ResponseEntity.ok(jwtTokenDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
