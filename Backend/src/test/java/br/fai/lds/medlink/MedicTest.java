package br.fai.lds.medlink;

import br.fai.lds.medlink.domain.enuns.Gender;
import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.PatientAuthorizedDto;
import br.fai.lds.medlink.implementation.service.medic.MedicServiceImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicTest {

    @Mock
    private MedicDao medicDao;

    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private MedicServiceImpl medicService;

    @Test
    public void testCreateMedic() {
        Medic medic = new Medic();
        medic.setId(1);
        medic.setName("Dr. João Silva");
        medic.setCpf("12345678901");
        medic.setPassword("123456");
        medic.setGender(Gender.MASCULINO);
        medic.setBirthDate(LocalDate.of(1980, 5, 15));
        medic.setEmail("dr.joao@medlink.com");
        medic.setPhoneNumber("11987654321");
        medic.setCrm("123456-SP");
        medic.setSpecialty("Cardiologia");
        medic.setActive(true);

        assertNotNull(medic);

        doAnswer(invocation -> {
            Medic m = invocation.getArgument(0);
            m.setId(medic.getId());
            return null;
        }).when(medicDao).create(any(Medic.class));

        int result = medicService.create(medic);

        assertEquals(medic.getId(), result);
    }

    @Test
    public void testCreateMedic_null_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicService.create(null));
    }

    @Test
    public void testCreateMedic_daoThrowsRuntimeException() {
        Medic medic = new Medic();
        medic.setName("Dr. Ana");

        doThrow(new RuntimeException("DB error")).when(medicDao).create(any(Medic.class));

        assertThrows(RuntimeException.class, () -> medicService.create(medic));
    }

    @Test
    public void testDelete_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicService.delete(0));
        assertThrows(IllegalArgumentException.class, () -> medicService.delete(-1));
    }

    @Test
    public void testDelete_success() {
        Medic medic = new Medic();
        medic.setId(1);
        medic.setActive(true);

        when(medicDao.readById(1)).thenReturn(medic);
        doNothing().when(medicDao).updateInformation(anyInt(), any(Medic.class));

        boolean result = medicService.delete(1);

        assertTrue(result);
        assertFalse(medic.isActive());
        verify(medicDao).updateInformation(1, medic);
    }

    @Test
    public void testDelete_medicNotFound_returnsFalse() {
        when(medicDao.readById(1)).thenReturn(null);

        boolean result = medicService.delete(1);

        assertFalse(result);
        verify(medicDao, never()).updateInformation(anyInt(), any(Medic.class));
    }

    @Test
    public void testFindById_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicService.findById(0));
        assertThrows(IllegalArgumentException.class, () -> medicService.findById(-1));
    }

    @Test
    public void testFindById_notFound_returnsNull() {
        when(medicDao.readById(1)).thenReturn(null);

        Medic result = medicService.findById(1);

        assertNull(result);
    }

    @Test
    public void testFindById_success() {
        Medic medic = new Medic();
        medic.setId(1);
        medic.setName("Dr. João");

        when(medicDao.readById(1)).thenReturn(medic);

        Medic result = medicService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Dr. João", result.getName());
    }

    @Test
    public void testFindAll_success() {
        List<Medic> medics = Arrays.asList(
            createMedic(1, "Dr. João"),
            createMedic(2, "Dr. Maria")
        );

        when(medicDao.readAll()).thenReturn(medics);

        List<Medic> result = medicService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dr. João", result.get(0).getName());
        assertEquals("Dr. Maria", result.get(1).getName());
    }

    @Test
    public void testUpdate_invalidId_throwsIllegalArgumentException() {
        Medic medic = new Medic();
        assertThrows(IllegalArgumentException.class, () -> medicService.update(0, medic));
        assertThrows(IllegalArgumentException.class, () -> medicService.update(-1, medic));
    }

    @Test
    public void testUpdate_nullMedic_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicService.update(1, null));
    }

    @Test
    public void testUpdate_medicNotFound_throwsIllegalArgumentException() {
        Medic medic = new Medic();
        medic.setName("Dr. Teste");

        when(medicDao.readById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> medicService.update(1, medic));
    }

    @Test
    public void testUpdate_success() {
        Medic existingMedic = createMedic(1, "Dr. João Antigo");
        Medic updatedData = new Medic();
        updatedData.setName("Dr. João Novo");
        updatedData.setCpf("98765432100");
        updatedData.setGender(Gender.MASCULINO);
        updatedData.setBirthDate(LocalDate.of(1985, 10, 20));
        updatedData.setPhoneNumber("11999888777");
        updatedData.setCrm("654321-SP");
        updatedData.setSpecialty("Neurologia");
        updatedData.setEmail("dr.joao.novo@medlink.com");
        updatedData.setActive(false);

        when(medicDao.readById(1)).thenReturn(existingMedic);
        doNothing().when(medicDao).updateInformation(anyInt(), any(Medic.class));

        Medic result = medicService.update(1, updatedData);

        assertNotNull(result);
        assertEquals("Dr. João Novo", result.getName());
        assertEquals("98765432100", result.getCpf());
        assertEquals("654321-SP", result.getCrm());
        assertEquals("Neurologia", result.getSpecialty());
        assertFalse(result.isActive());
        verify(medicDao).updateInformation(1, existingMedic);
    }

    @Test
    public void testFindByIds_nullOrEmptyList_returnsEmptyMap() {
        Map<Integer, Medic> result1 = medicService.findByIds(null);
        Map<Integer, Medic> result2 = medicService.findByIds(Arrays.asList());

        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }

    @Test
    public void testFindByIds_success() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        Medic medic1 = createMedic(1, "Dr. João");
        Medic medic2 = createMedic(2, "Dr. Maria");

        when(medicDao.readById(1)).thenReturn(medic1);
        when(medicDao.readById(2)).thenReturn(medic2);
        when(medicDao.readById(3)).thenReturn(null);

        Map<Integer, Medic> result = medicService.findByIds(ids);

        assertEquals(2, result.size());
        assertEquals("Dr. João", result.get(1).getName());
        assertEquals("Dr. Maria", result.get(2).getName());
        assertNull(result.get(3));
    }

    @Test
    public void testFindAuthorizedPatients_success() {
        int medicId = 1;
        List<Patient> patients = Arrays.asList(
            createPatient(1, "Paciente 1", "paciente1@email.com"),
            createPatient(2, "Paciente 2", "paciente2@email.com")
        );

        when(patientDao.findAuthorizedByMedicId(medicId)).thenReturn(patients);

        List<PatientAuthorizedDto> result = medicService.findAuthorizedPatients(medicId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Paciente 1", result.get(0).getName());
        assertEquals("Paciente 2", result.get(1).getName());
        assertEquals("paciente1@email.com", result.get(0).getEmail());
        assertEquals("paciente2@email.com", result.get(1).getEmail());
    }

    private Medic createMedic(int id, String name) {
        Medic medic = new Medic();
        medic.setId(id);
        medic.setName(name);
        medic.setCpf("12345678901");
        medic.setGender(Gender.MASCULINO);
        medic.setBirthDate(LocalDate.of(1980, 1, 1));
        medic.setEmail("medico@email.com");
        medic.setPhoneNumber("11999999999");
        medic.setCrm("123456-SP");
        medic.setSpecialty("Clínica Geral");
        medic.setActive(true);
        return medic;
    }

    private Patient createPatient(int id, String name, String email) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setEmail(email);
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        return patient;
    }
}