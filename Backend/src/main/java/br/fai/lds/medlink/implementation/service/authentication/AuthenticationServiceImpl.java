package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Profile("basic")
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private MedicDao medicDao;

    // Mapa simples para armazenar códigos temporários (email -> código)
    private final Map<String, String> resetCodes = new HashMap<>();

    @Override
    public Patient authenticatePatient(String email, String password) {
        log.info("Tentando autenticar paciente com email: {}", email);
        Patient patient = patientDao.findByEmailAndPassword(email, password);
        if (patient != null) {
            log.info("Paciente encontrado: {}", patient.getName());
            log.info("Autenticação bem-sucedida para paciente: {}", patient.getName());
            return patient;
        } else {
            log.warn("Paciente não encontrado ou senha incorreta com email: {}", email);
        }
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        log.info("=== AUTENTICANDO MÉDICO ===");
        log.info("Email: {}", email);
        Medic medic = medicDao.findByEmailAndPassword(email, password);
        if (medic != null) {
            log.info("✅ Autenticação bem-sucedida para médico ID: {}", medic.getId());
            return medic;
        } else {
            log.warn("❌ Email ou senha incorretos para médico: {}", email);
        }
        return null;
    }

    @Override
    public boolean requestPasswordReset(PasswordResetRequestDTO dto) {
        String email = dto.getIdentifier();

        Patient patient = patientDao.findByEmail(email);
        Medic medic = medicDao.findByEmail(email);

        if (patient == null && medic == null) {
            return false; // email não encontrado
        }

        String resetCode = generateResetCode();
        resetCodes.put(email, resetCode);

        // Simula envio do código por email
        log.info("Enviando código de reset '{}' para o email: {}", resetCode, email);

        return true;
    }

    @Override
    public boolean resetPassword(PasswordResetDTO dto) {
        String email = dto.getIdentifier();
        String code = dto.getVerificationCode();
        String newPassword = dto.getNewPassword();

        if (!validateResetCode(email, code)) {
            return false; // código inválido
        }

        Patient patient = patientDao.findByEmail(email);
        if (patient != null) {
            patient.setPassword(newPassword);
            patientDao.updateInformation(patient.getId(), patient);
            invalidateResetCode(email);
            return true;
        }

        Medic medic = medicDao.findByEmail(email);
        if (medic != null) {
            medic.setPassword(newPassword);
            medicDao.updateInformation(patient.getId(), medic);
            invalidateResetCode(email);
            return true;
        }

        return false;
    }

    @Override
    public boolean sendVerificationCode(String identifier) {
        Patient patient = patientDao.findByEmail(identifier);
        Medic medic = medicDao.findByEmail(identifier);

        if (patient == null && medic == null) {
            return false; // usuário não encontrado
        }

        String code = generateResetCode();
        resetCodes.put(identifier, code);

        log.info("Enviando código de verificação '{}' para: {}", code, identifier);

        return true;
    }

    // Métodos auxiliares

    private String generateResetCode() {
        int code = (int) (Math.random() * 900000) + 100000; // 6 dígitos aleatórios
        return String.valueOf(code);
    }

    private boolean validateResetCode(String email, String code) {
        if (!resetCodes.containsKey(email)) {
            return false;
        }
        return resetCodes.get(email).equals(code);
    }

    private void invalidateResetCode(String email) {
        resetCodes.remove(email);
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
