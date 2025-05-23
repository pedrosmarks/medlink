package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordFakeDaoImpl implements MedicalRecordDao {

    private static List<MedicalRecord> medicalRecord = new ArrayList<>();
    private static int ID = 1;

    private int getNextId() {

        return ID++;
    }

    public MedicalRecordFakeDaoImpl() {

        medicalRecord.add(MedicalRecord.builder()
                .medications("Losartana")
                .allergies("Dipirona")
                .bloodType(BloodType.A_POSITIVE)
                .vaccine("Covid 19")
                .diagnosis("Pressão alta")
                .organDonor("SIM")
                .familyHistory("Não há")
                .surgicalHistory("Não há")
                .build());

    }

    @Override
    public void create(MedicalRecord entity) {
        entity.setId(getNextId());
        medicalRecord.add(entity);

    }

    @Override
    public boolean remove(int id) {
        MedicalRecord medicalRecord = readById(id);
        if (medicalRecord != null) {
            medicalRecord.setMedicalRecordActive(false);
        }
        return false;
    }

    @Override
    public MedicalRecord readById(int id) {
        return medicalRecord
                .stream()
                .filter(medicalRecord -> medicalRecord.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<MedicalRecord> readAll() {
        return medicalRecord;

    }

    @Override
    public void updateInformation(int id, MedicalRecord entity) {
        for (int i = 0; i < medicalRecord.size(); i++) {
            if (medicalRecord.get(i).getId() == id) {
                medicalRecord.set(i, entity);
                return;
            }
        }
    }
}