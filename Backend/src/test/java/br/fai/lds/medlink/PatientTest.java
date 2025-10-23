package br.fai.lds.medlink;

import br.fai.lds.medlink.domain.Gender;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.implementation.service.pacient.PatientServiceImpl;
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
public class PatientTest {

    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void testCreatePatient() {

        Patient patient = new Patient();
        patient.setId(1);
        patient.setName("João");
        patient.setCpf("07277766652");
        patient.setPassword("123456");
        patient.setGender(Gender.MASCULINO);
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setEmail("joao@example.com");
        patient.setPhoneNumber("11999999999");

        assertNotNull(patient);

        // stub: o DAO.create é void, então usamos doAnswer para simular a atribuição do id
        doAnswer(invocation -> {
            Patient p = invocation.getArgument(0);
            p.setId(patient.getId()); // simula comportamento do DAO ao persistir e atribuir id
            return null;
        }).when(patientDao).create(any(Patient.class));

        int result = patientService.create(patient);

        assertEquals(patient.getId(), result);
    }

    @Test
    public void testCreatePatient_null_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> patientService.create(null));
    }

    @Test
    public void testCreatePatient_daoThrowsRuntimeException() {
        Patient patient = new Patient();
        patient.setName("Ana");

        doThrow(new RuntimeException("DB error")).when(patientDao).create(any(Patient.class));

        assertThrows(RuntimeException.class, () -> patientService.create(patient));
    }

    @Test
    public void testRemove_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> patientService.remove(0));
    }

    @Test
    public void testRemove_success_and_failure() {
        when(patientDao.remove(1)).thenReturn(true);
        when(patientDao.remove(2)).thenReturn(false);

        assertTrue(patientService.remove(1));
        assertFalse(patientService.remove(2));
    }

    @Test
    public void testFindById_notFound_returnsNull() {
        when(patientDao.readById(1)).thenReturn(null);

        Patient p = patientService.findById(1);

        assertNull(p);
    }

    @Test
    public void testUpdateInformation_nonexistentPatient_throws() {
        Patient patient = new Patient();
        patient.setName("Teste");

        when(patientDao.readById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> patientService.updateInformation(1, patient));
    }

    @Test
    public void testSendAccessRequest_callsDao() {
        doNothing().when(patientDao).createAccessRequest(anyInt(), anyInt());

        patientService.sendAccessRequest(5, 10);

        verify(patientDao).createAccessRequest(5, 10);
    }

}

