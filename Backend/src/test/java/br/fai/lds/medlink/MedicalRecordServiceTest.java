package br.fai.lds.medlink;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.domain.enuns.BloodType;
import br.fai.lds.medlink.domain.enuns.OrganDonorStatus;
import br.fai.lds.medlink.implementation.service.medicalRecord.MedicalRecordServiceImpl;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordDao medicalRecordDao;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @Test
    public void testCreate_success() {
        MedicalRecord record = createMedicalRecord(1, 1);

        doAnswer(invocation -> {
            MedicalRecord r = invocation.getArgument(0);
            r.setId(record.getId());
            return null;
        }).when(medicalRecordDao).create(any(MedicalRecord.class));

        int result = medicalRecordService.create(record);

        assertEquals(record.getId(), result);
        verify(medicalRecordDao).create(record);
    }

    @Test
    public void testCreate_nullRecord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.create(null));
    }

    @Test
    public void testDelete_success() {
        MedicalRecord record = createMedicalRecord(1, 1);
        record.setMedicalRecordActive(true);

        when(medicalRecordDao.readById(1)).thenReturn(record);
        doNothing().when(medicalRecordDao).updateInformation(anyInt(), any(MedicalRecord.class));

        boolean result = medicalRecordService.delete(1);

        assertTrue(result);
        assertFalse(record.isMedicalRecordActive());
        verify(medicalRecordDao).updateInformation(1, record);
    }

    @Test
    public void testDelete_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.delete(0));
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.delete(-1));
    }

    @Test
    public void testFindById_success() {
        MedicalRecord record = createMedicalRecord(1, 1);

        when(medicalRecordDao.readById(1)).thenReturn(record);

        MedicalRecord result = medicalRecordService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getPatientId());
    }

    @Test
    public void testFindById_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.findById(0));
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.findById(-1));
    }

    @Test
    public void testFindAll_success() {
        List<MedicalRecord> records = Arrays.asList(
            createMedicalRecord(1, 1),
            createMedicalRecord(2, 2)
        );

        when(medicalRecordDao.readAll()).thenReturn(records);

        List<MedicalRecord> result = medicalRecordService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    public void testFindByPatientId_success() {
        MedicalRecord record = createMedicalRecord(1, 1);

        when(medicalRecordDao.findByPatientId(1)).thenReturn(record);

        MedicalRecordResponseDto result = medicalRecordService.findByPatientId(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getPatientId());
        assertEquals(BloodType.A_POSITIVE, result.getBloodType());
        assertEquals(OrganDonorStatus.SIM, result.getOrganDonor());
    }

    @Test
    public void testFindByPatientId_invalidMedicId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.findByPatientId(0, 1));
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.findByPatientId(-1, 1));
    }

    @Test
    public void testAddConsultation_success() {
        MedicalRecord record = createMedicalRecord(1, 1);
        Consultation consultation = createConsultation();

        when(medicalRecordDao.readById(1)).thenReturn(record);
        doNothing().when(medicalRecordDao).updateInformation(anyInt(), any(MedicalRecord.class));

        boolean result = medicalRecordService.addConsultation(1, consultation);

        assertTrue(result);
        assertTrue(record.getConsultations().contains(consultation));
        verify(medicalRecordDao).updateInformation(1, record);
    }

    @Test
    public void testAddConsultation_invalidId_throwsIllegalArgumentException() {
        Consultation consultation = createConsultation();
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.addConsultation(0, consultation));
    }

    @Test
    public void testAddMedication_success() {
        MedicalRecord record = createMedicalRecord(1, 1);
        Medication medication = createMedication();

        when(medicalRecordDao.readById(1)).thenReturn(record);
        doNothing().when(medicalRecordDao).updateInformation(anyInt(), any(MedicalRecord.class));

        boolean result = medicalRecordService.addMedication(1, medication);

        assertTrue(result);
        assertTrue(record.getMedications().contains(medication));
        verify(medicalRecordDao).updateInformation(1, record);
    }

    @Test
    public void testAddAllergy_success() {
        MedicalRecord record = createMedicalRecord(1, 1);
        Allergy allergy = createAllergy();

        when(medicalRecordDao.readById(1)).thenReturn(record);
        doNothing().when(medicalRecordDao).updateInformation(anyInt(), any(MedicalRecord.class));

        boolean result = medicalRecordService.addAllergy(1, allergy);

        assertTrue(result);
        assertTrue(record.getAllergies().contains(allergy));
        verify(medicalRecordDao).updateInformation(1, record);
    }

    @Test
    public void testAddFamilyHistory_success() {
        MedicalRecord record = createMedicalRecord(1, 1);
        String familyHistory = "Histórico familiar atualizado";

        when(medicalRecordDao.readById(1)).thenReturn(record);
        doNothing().when(medicalRecordDao).updateInformation(anyInt(), any(MedicalRecord.class));

        boolean result = medicalRecordService.addFamilyHistory(1, familyHistory);

        assertTrue(result);
        assertEquals(familyHistory, record.getFamilyHistory());
        verify(medicalRecordDao).updateInformation(1, record);
    }

    @Test
    public void testAddFamilyHistory_nullOrEmpty_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.addFamilyHistory(1, null));
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.addFamilyHistory(1, ""));
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.addFamilyHistory(1, "   "));
    }

    // Métodos auxiliares
    private MedicalRecord createMedicalRecord(int id, int patientId) {
        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setPatientId(patientId);
        record.setBloodType(BloodType.A_POSITIVE);
        record.setOrganDonor(OrganDonorStatus.SIM);
        record.setDiagnosis("Diagnóstico inicial");
        record.setFamilyHistory("Histórico familiar inicial");
        record.setMedicalRecordActive(true);
        return record;
    }

    private Consultation createConsultation() {
        Consultation consultation = new Consultation();
        consultation.setDate(LocalDate.now());
        consultation.setReason("Consulta de rotina");
        consultation.setNotes("Paciente em bom estado geral");
        return consultation;
    }

    private Medication createMedication() {
        Medication medication = new Medication();
        medication.setName("Paracetamol");
        medication.setDosage("500mg");
        medication.setFrequency("8/8h");
        return medication;
    }

    private Allergy createAllergy() {
        Allergy allergy = new Allergy();
        allergy.setName("Alergia a Penicilina");
        allergy.setSubstance("Penicilina");
        allergy.setReaction("Erupção cutânea");
        allergy.setSeverity("Moderada");
        return allergy;
    }
}