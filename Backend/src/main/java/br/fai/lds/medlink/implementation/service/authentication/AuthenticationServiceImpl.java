package br.fai.lds.medlink.implementation.service.authentication;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PatientService patientService;
    private final MedicService medicService;

    public AuthenticationServiceImpl(MedicService medicService, PatientService patientService) {
        this.medicService = medicService;
        this.patientService = patientService;
    }

    // Mapa simples para armazenar códigos temporários (email -> código)
    private final Map<String, String> resetCodes = new HashMap<>();

    @Override
    public Object authenticate(String email, String password) {
        // Tenta encontrar como paciente primeiro
        Patient patient = authenticatePatient(email, password);
        if (patient != null) {
            return patient;
        }

        // Se não for paciente, tenta como médico
        Medic medic = authenticateMedic(email, password);
        if (medic != null) {
            return medic;
        }

        return null;
    }

    @Override
    public Patient authenticatePatient(String email, String password) {
        Patient patient = patientService.findByEmail(email);
        if (patient != null && password.equals(patient.getPassword())) {
            return patient;
        }
        return null;
    }

    @Override
    public Medic authenticateMedic(String email, String password) {
        var medics = medicService.findAll();
        var medic = medics.stream()
                .filter(m -> email.equals(m.getEmail()) && password.equals(m.getPassword()))
                .findFirst()
                .orElse(null);
        return medic;
    }

    @Override
    public boolean requestPasswordReset(PasswordResetRequestDTO dto) {
        String email = dto.getIdentifier();

        Patient patient = patientService.findByEmail(email);
        var medics = medicService.findAll();
        Medic medic = medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);

        if (patient == null && medic == null) {
            return false; // email não encontrado
        }

        String resetCode = generateResetCode();
        resetCodes.put(email, resetCode);

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

        Patient patient = patientService.findByEmail(email);
        if (patient != null) {
            patient.setPassword(newPassword);
            patientService.updateInformation(patient.getId(), patient);
            invalidateResetCode(email);
            return true;
        }

        var medics = medicService.findAll();
        Medic medic = medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);
        if (medic != null) {
            medic.setPassword(newPassword);
            medicService.update(medic.getId(), medic);
            invalidateResetCode(email);
            return true;
        }

        return false;
    }

    @Override
    public boolean sendVerificationCode(String identifier) {
        Patient patient = patientService.findByEmail(identifier);
        var medics = medicService.findAll();
        Medic medic = medics.stream()
                .filter(m -> identifier.equals(m.getEmail()))
                .findFirst()
                .orElse(null);

        if (patient == null && medic == null) {
            return false; // usuário não encontrado
        }

        String code = generateResetCode();
        resetCodes.put(identifier, code);

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
        return patientService.findByEmail(email);
    }

    @Override
    public Medic findMedicByEmail(String email) {
        var medics = medicService.findAll();
        return medics.stream()
                .filter(m -> email.equals(m.getEmail()))
                .findFirst()
                .orElse(null);
    }
}