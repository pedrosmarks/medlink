package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtAuthenticationServiceImpl implements AuthenticationService {

    private final PatientDao patientDao;
    private final MedicDao medicDao;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationServiceImpl(PatientDao patientDao, MedicDao medicDao, PasswordEncoder passwordEncoder) {
        this.patientDao = patientDao;
        this.medicDao = medicDao;
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
        Patient patient = patientDao.findByEmailAndPassword(email, password);
        if (patient != null && passwordEncoder.matches(password, patient.getPassword())) {
            return patient;
        }
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        Medic medic = medicDao.findByEmailAndPassword(email, password);
        if (medic != null && passwordEncoder.matches(password, medic.getPassword())) {
            return medic;
        }
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
        return patientDao.findByEmail(email);
    }

    @Override
    public Medic findMedicByEmail(String email) {
        return medicDao.findByEmail(email);
    }
}
