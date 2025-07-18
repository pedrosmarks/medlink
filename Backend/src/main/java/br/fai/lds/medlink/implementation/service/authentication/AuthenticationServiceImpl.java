package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        Patient patient = patientDao.findByEmail(email);
        if (patient != null && patient.getPassword().equals(password)) {
            return patient;
        }
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        Medic medic = medicDao.findByEmail(email);
        if (medic != null && medic.getPassword().equals(password)) {
            return medic;
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
        System.out.println("Enviando código de reset '" + resetCode + "' para o email: " + email);

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

        System.out.println("Enviando código de verificação '" + code + "' para: " + identifier);

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
}
