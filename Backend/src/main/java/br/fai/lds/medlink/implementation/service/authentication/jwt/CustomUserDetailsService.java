package br.fai.lds.medlink.implementation.service.authentication.jwt;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("jwt")
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PatientService patientService;
    private final MedicService medicService;

    public CustomUserDetailsService(PatientService patientService, MedicService medicService) {
        this.patientService = patientService;
        this.medicService = medicService;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService - Carregando usuário: " + email);
        
        // Tenta encontrar como paciente primeiro
        Patient patient = patientService.findByEmail(email);
        if (patient != null) {
            System.out.println("Usuário encontrado como PACIENTE: " + patient.getName());
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("PATIENT")
            );
            return new User(
                    patient.getEmail(),
                    patient.getPassword(),
                    authorities
            );
        }

        // Se não for paciente, tenta como médico
        var medics = medicService.findAll();
        Medic medic = medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);
        
        if (medic != null) {
            System.out.println("Usuário encontrado como MEDICO: " + medic.getName());
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("MEDIC")
            );
            return new User(
                    medic.getEmail(),
                    medic.getPassword(),
                    authorities
            );
        }

        System.out.println("Usuário NÃO encontrado: " + email);
        throw new UsernameNotFoundException("Email não encontrado");
    }
}
