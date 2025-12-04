package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtAuthenticationServiceImpl implements AuthenticationService {

    private final PatientService patientService;
    private final MedicService medicService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationServiceImpl(MedicService medicService, PatientService patientService, PasswordEncoder passwordEncoder) {
        this.medicService = medicService;
        this.patientService = patientService;
        this.passwordEncoder = passwordEncoder;
    }

    public Object authenticate(String email, String password) {
        // Tenta encontrar como paciente primeiro
        var patient = findPatientByEmail(email);
        if (patient != null) {
            if(!passwordEncoder.matches(password, patient.getPassword())){
                throw new BadCredentialsException("credenciais invalidas!");
            }
            return patient;
        }

        // Se não for paciente, tenta como médico
        var medic = findMedicByEmail(email);
        if (medic != null) {
            if(!passwordEncoder.matches(password, medic.getPassword())){
                throw new BadCredentialsException("credenciais invalidas!");
            }
            return medic;
        }

        throw new UsernameNotFoundException("usuario nao encontrado");
    }

    @Override
    public Patient authenticatePatient(String email, String password) {
        Patient patient = patientService.findByEmail(email);
        if (patient != null && passwordEncoder.matches(password, patient.getPassword())) {
            System.out.println("Paciente autenticado: " + patient.getName());
            return patient;
        }
        System.out.println("Falha na autenticação do paciente: " + email);
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        // Precisa implementar findByEmail no MedicService
        // Por enquanto, vamos buscar todos e filtrar
        var medics = medicService.findAll();
        var medic = medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);
        
        if (medic != null && passwordEncoder.matches(password, medic.getPassword())) {
            System.out.println("Médico autenticado: " + medic.getName());
            return medic;
        }
        System.out.println("Falha na autenticação do médico: " + email);
        return null;
    }

    @Override
    public boolean requestPasswordReset(PasswordResetRequestDTO dto) {
        return false;
    }

    @Override
    public boolean resetPassword(PasswordResetDTO dto) {
        return false;
    }

    @Override
    public boolean sendVerificationCode(String identifier) {
        return false;
    }

    @Override
    public Patient findPatientByEmail(String email) {
        return patientService.findByEmail(email);
    }

    @Override
    public Medic findMedicByEmail(String email) {
        // Precisa implementar findByEmail no MedicService
        var medics = medicService.findAll();
        return medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
