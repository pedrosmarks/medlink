package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class MedicalRecordFakeDaoImpl implements MedicalRecordDao {

    private static List<MedicalRecord> medicalRecords = new ArrayList<>();
    private static int ID = 1;

    private int getNextId() {
        return ID++;
    }

    public MedicalRecordFakeDaoImpl() {
        medicalRecords.add(MedicalRecord.builder()
                .id(getNextId())
                .patientId(1)
                .medications(List.of(
                        Medication.builder()
                                .name("Losartana")
                                .dosage("50mg")
                                .frequency("1x dia")
                                .build()
                ))
                .allergies(List.of(
                        Allergy.builder()
                                .name("Dipirona")
                                .substance("Dipyrone")
                                .reaction("Erupção cutânea")
                                .severity("Média")
                                .build()
                ))
                .vaccines(List.of(
                        Vaccine.builder()
                                .name("Covid-19")
                                .date("2021-05-20")
                                .build()
                ))
                .surgeries(List.of(
                        Surgery.builder()
                                .name("Apendicectomia")
                                .date("2010-01-01")
                                .location("Hospital XYZ")
                                .notes("Sem complicações")
                                .build()
                ))
                .consultations(new ArrayList<>()) // inicializa vazio
                .bloodType(BloodType.A_POSITIVE)
                .organDonor(OrganDonorStatus.SIM)
                .diagnosis("Pressão alta")
                .familyHistory("Hipertensão na família")
                .medicalRecordActive(true)
                .build());
    }

    @Override
    public void create(MedicalRecord entity) {
        entity.setId(getNextId());
        medicalRecords.add(entity);
    }

    @Override
    public boolean remove(int id) {
        MedicalRecord record = readById(id);
        if (record != null) {
            record.setMedicalRecordActive(false);
            return true;
        }
        return false;
    }

    @Override
    public MedicalRecord readById(int id) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<MedicalRecord> readAll() {
        return medicalRecords;
    }

    @Override
    public void updateInformation(int id, MedicalRecord entity) {
        for (int i = 0; i < medicalRecords.size(); i++) {
            if (medicalRecords.get(i).getId() == id) {
                medicalRecords.set(i, entity);
                return;
            }
        }
    }

    @Override
    public MedicalRecord findByPatientId(int patientId) {
        return medicalRecords.stream()
                .filter(record -> record.getPatientId() == patientId)
                .findFirst()
                .orElse(null);
    }
}
