package br.fai.lds.medlink;

import br.fai.lds.medlink.domain.enuns.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetDTO;
import br.fai.lds.medlink.domain.dataTransferObject.Login.PasswordResetRequestDTO;
import br.fai.lds.medlink.implementation.service.authentication.AuthenticationServiceImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private PatientDao patientDao;

    @Mock
    private MedicDao medicDao;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testAuthenticatePatient_success() {
        String email = "paciente@email.com";
        String password = "123456";
        Patient patient = createPatient(1, "João Silva", email);

        when(patientDao.findByEmailAndPassword(email, password)).thenReturn(patient);

        Patient result = authenticationService.authenticatePatient(email, password);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("João Silva", result.getName());
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testAuthenticatePatient_invalidCredentials_returnsNull() {
        String email = "paciente@email.com";
        String password = "senhaErrada";

        when(patientDao.findByEmailAndPassword(email, password)).thenReturn(null);

        Patient result = authenticationService.authenticatePatient(email, password);

        assertNull(result);
    }

    @Test
    public void testAuthenticateMedic_success() {
        String email = "medico@email.com";
        String password = "123456";
        Medic medic = createMedic(1, "Dr. Maria", email);

        when(medicDao.findByEmailAndPassword(email, password)).thenReturn(medic);

        Medic result = authenticationService.authenticateMedic(email, password);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Dr. Maria", result.getName());
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testAuthenticateMedic_invalidCredentials_returnsNull() {
        String email = "medico@email.com";
        String password = "senhaErrada";

        when(medicDao.findByEmailAndPassword(email, password)).thenReturn(null);

        Medic result = authenticationService.authenticateMedic(email, password);

        assertNull(result);
    }

    @Test
    public void testRequestPasswordReset_patientExists_returnsTrue() {
        String email = "paciente@email.com";
        Patient patient = createPatient(1, "João", email);
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
        dto.setIdentifier(email);

        when(patientDao.findByEmail(email)).thenReturn(patient);
        when(medicDao.findByEmail(email)).thenReturn(null);

        boolean result = authenticationService.requestPasswordReset(dto);

        assertTrue(result);
    }

    @Test
    public void testRequestPasswordReset_medicExists_returnsTrue() {
        String email = "medico@email.com";
        Medic medic = createMedic(1, "Dr. João", email);
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
        dto.setIdentifier(email);

        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(medic);

        boolean result = authenticationService.requestPasswordReset(dto);

        assertTrue(result);
    }

    @Test
    public void testRequestPasswordReset_userNotFound_returnsFalse() {
        String email = "inexistente@email.com";
        PasswordResetRequestDTO dto = new PasswordResetRequestDTO();
        dto.setIdentifier(email);

        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(null);

        boolean result = authenticationService.requestPasswordReset(dto);

        assertFalse(result);
    }

    @Test
    public void testResetPassword_patientWithValidCode_returnsTrue() {
        String email = "paciente@email.com";
        String code = "123456";
        String newPassword = "novaSenha";
        Patient patient = createPatient(1, "João", email);
        
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode(code);
        dto.setNewPassword(newPassword);

        // Primeiro, simula a geração do código
        when(patientDao.findByEmail(email)).thenReturn(patient);
        when(medicDao.findByEmail(email)).thenReturn(null);
        authenticationService.sendVerificationCode(email);

        // Agora testa o reset
        doNothing().when(patientDao).updateInformation(anyInt(), any(Patient.class));

        boolean result = authenticationService.resetPassword(dto);

        assertTrue(result);
        assertEquals(newPassword, patient.getPassword());
        verify(patientDao).updateInformation(1, patient);
    }

    @Test
    public void testResetPassword_medicWithValidCode_returnsTrue() {
        String email = "medico@email.com";
        String code = "123456";
        String newPassword = "novaSenha";
        Medic medic = createMedic(1, "Dr. João", email);
        
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode(code);
        dto.setNewPassword(newPassword);

        // Primeiro, simula a geração do código
        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(medic);
        authenticationService.sendVerificationCode(email);

        // Agora testa o reset
        doNothing().when(medicDao).updateInformation(anyInt(), any(Medic.class));

        boolean result = authenticationService.resetPassword(dto);

        assertTrue(result);
        assertEquals(newPassword, medic.getPassword());
        verify(medicDao).updateInformation(1, medic);
    }

    @Test
    public void testResetPassword_invalidCode_returnsFalse() {
        String email = "paciente@email.com";
        String invalidCode = "999999";
        String newPassword = "novaSenha";
        
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode(invalidCode);
        dto.setNewPassword(newPassword);

        boolean result = authenticationService.resetPassword(dto);

        assertFalse(result);
        verify(patientDao, never()).updateInformation(anyInt(), any(Patient.class));
        verify(medicDao, never()).updateInformation(anyInt(), any(Medic.class));
    }

    @Test
    public void testSendVerificationCode_patientExists_returnsTrue() {
        String email = "paciente@email.com";
        Patient patient = createPatient(1, "João", email);

        when(patientDao.findByEmail(email)).thenReturn(patient);
        when(medicDao.findByEmail(email)).thenReturn(null);

        boolean result = authenticationService.sendVerificationCode(email);

        assertTrue(result);
    }

    @Test
    public void testSendVerificationCode_medicExists_returnsTrue() {
        String email = "medico@email.com";
        Medic medic = createMedic(1, "Dr. João", email);

        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(medic);

        boolean result = authenticationService.sendVerificationCode(email);

        assertTrue(result);
    }

    @Test
    public void testSendVerificationCode_userNotFound_returnsFalse() {
        String email = "inexistente@email.com";

        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(null);

        boolean result = authenticationService.sendVerificationCode(email);

        assertFalse(result);
    }

    @Test
    public void testFindPatientByEmail_success() {
        String email = "paciente@email.com";
        Patient patient = createPatient(1, "João", email);

        when(patientDao.findByEmail(email)).thenReturn(patient);

        Patient result = authenticationService.findPatientByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("João", result.getName());
    }

    @Test
    public void testFindPatientByEmail_notFound_returnsNull() {
        String email = "inexistente@email.com";

        when(patientDao.findByEmail(email)).thenReturn(null);

        Patient result = authenticationService.findPatientByEmail(email);

        assertNull(result);
    }

    @Test
    public void testFindMedicByEmail_success() {
        String email = "medico@email.com";
        Medic medic = createMedic(1, "Dr. João", email);

        when(medicDao.findByEmail(email)).thenReturn(medic);

        Medic result = authenticationService.findMedicByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Dr. João", result.getName());
    }

    @Test
    public void testFindMedicByEmail_notFound_returnsNull() {
        String email = "inexistente@email.com";

        when(medicDao.findByEmail(email)).thenReturn(null);

        Medic result = authenticationService.findMedicByEmail(email);

        assertNull(result);
    }

    @Test
    public void testAuthenticatePatient_nullEmail_returnsNull() {
        Patient result = authenticationService.authenticatePatient(null, "123456");
        assertNull(result);
    }

    @Test
    public void testAuthenticatePatient_nullPassword_returnsNull() {
        Patient result = authenticationService.authenticatePatient("email@test.com", null);
        assertNull(result);
    }

    @Test
    public void testAuthenticateMedic_nullEmail_returnsNull() {
        Medic result = authenticationService.authenticateMedic(null, "123456");
        assertNull(result);
    }

    @Test
    public void testAuthenticateMedic_nullPassword_returnsNull() {
        Medic result = authenticationService.authenticateMedic("email@test.com", null);
        assertNull(result);
    }

    @Test
    public void testResetPassword_userNotFoundAfterCodeGeneration_returnsFalse() {
        String email = "inexistente@email.com";
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode("123456");
        dto.setNewPassword("novaSenha");

        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(null);

        boolean result = authenticationService.resetPassword(dto);

        assertFalse(result);
    }

    @Test
    public void testResetPassword_patientUpdateFails_returnsFalse() {
        String email = "paciente@email.com";
        String code = "123456";
        Patient patient = createPatient(1, "João", email);
        
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode(code);
        dto.setNewPassword("novaSenha");

        // Simula geração do código
        when(patientDao.findByEmail(email)).thenReturn(patient);
        when(medicDao.findByEmail(email)).thenReturn(null);
        authenticationService.sendVerificationCode(email);

        // Simula falha na atualização
        doThrow(new RuntimeException("Update failed")).when(patientDao).updateInformation(anyInt(), any(Patient.class));

        assertThrows(RuntimeException.class, () -> authenticationService.resetPassword(dto));
    }

    @Test
    public void testResetPassword_medicUpdateFails_returnsFalse() {
        String email = "medico@email.com";
        String code = "123456";
        Medic medic = createMedic(1, "Dr. João", email);
        
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier(email);
        dto.setVerificationCode(code);
        dto.setNewPassword("novaSenha");

        // Simula geração do código
        when(patientDao.findByEmail(email)).thenReturn(null);
        when(medicDao.findByEmail(email)).thenReturn(medic);
        authenticationService.sendVerificationCode(email);

        // Simula falha na atualização
        doThrow(new RuntimeException("Update failed")).when(medicDao).updateInformation(anyInt(), any(Medic.class));

        assertThrows(RuntimeException.class, () -> authenticationService.resetPassword(dto));
    }

    @Test
    public void testResetPassword_emptyCode_returnsFalse() {
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier("email@test.com");
        dto.setVerificationCode("");
        dto.setNewPassword("novaSenha");

        boolean result = authenticationService.resetPassword(dto);

        assertFalse(result);
    }

    @Test
    public void testResetPassword_nullCode_returnsFalse() {
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setIdentifier("email@test.com");
        dto.setVerificationCode(null);
        dto.setNewPassword("novaSenha");

        boolean result = authenticationService.resetPassword(dto);

        assertFalse(result);
    }

    @Test
    public void testSendVerificationCode_nullIdentifier_returnsFalse() {
        boolean result = authenticationService.sendVerificationCode(null);
        assertFalse(result);
    }

    @Test
    public void testSendVerificationCode_emptyIdentifier_returnsFalse() {
        boolean result = authenticationService.sendVerificationCode("");
        assertFalse(result);
    }

    @Test
    public void testFindPatientByEmail_nullEmail_returnsNull() {
        Patient result = authenticationService.findPatientByEmail(null);
        assertNull(result);
    }

    @Test
    public void testFindMedicByEmail_nullEmail_returnsNull() {
        Medic result = authenticationService.findMedicByEmail(null);
        assertNull(result);
    }

    @Test
    public void testRequestPasswordReset_nullDto_returnsFalse() {
        boolean result = authenticationService.requestPasswordReset(null);
        assertFalse(result);
    }

    @Test
    public void testResetPassword_nullDto_returnsFalse() {
        boolean result = authenticationService.resetPassword(null);
        assertFalse(result);
    }

    // Métodos auxiliares
    private Patient createPatient(int id, String name, String email) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setEmail(email);
        patient.setCpf("12345678901");
        patient.setPassword("123456");
        patient.setGender(Gender.MASCULINO);
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setPhoneNumber("11999999999");
        return patient;
    }

    private Medic createMedic(int id, String name, String email) {
        Medic medic = new Medic();
        medic.setId(id);
        medic.setName(name);
        medic.setEmail(email);
        medic.setCpf("12345678901");
        medic.setPassword("123456");
        medic.setGender(Gender.MASCULINO);
        medic.setBirthDate(LocalDate.of(1980, 1, 1));
        medic.setPhoneNumber("11999999999");
        medic.setCrm("123456-SP");
        medic.setSpecialty("Cardiologia");
        medic.setActive(true);
        return medic;
    }
}