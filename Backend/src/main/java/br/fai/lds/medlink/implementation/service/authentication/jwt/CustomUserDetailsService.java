package br.fai.lds.medlink.implementation.service.authentication.jwt;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
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

    private final PatientDao patientDao;
    private final MedicDao medicDao;

    public CustomUserDetailsService(PatientDao patientDao, MedicDao medicDao) {
        this.patientDao = patientDao;
        this.medicDao = medicDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        // Tenta encontrar como paciente primeiro
        Patient patient = patientDao.findByEmail(email);
        if (patient != null) {
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
        Medic medic = medicDao.findByEmail(email);
        if (medic != null) {
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("MEDIC")
            );
            return new User(
                    medic.getEmail(),
                    medic.getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("Email não encontrado");
    }
}
