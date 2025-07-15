package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.BloodType;
import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.OrganDonorStatus;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordFakeDaoImpl implements MedicalRecordDao {

    private static List<MedicalRecord> medicalRecords = new ArrayList<>();
    private static int ID = 1;

    private int getNextId() {
        return ID++;
    }

    public MedicalRecordFakeDaoImpl() {
        medicalRecords.add(MedicalRecord.builder()
                .id(getNextId())
                .medications("Losartana")
                .allergies("Dipirona")
                .bloodType(BloodType.A_POSITIVE)
                .vaccine("Covid-19")
                .diagnosis("Pressão alta")
                .organDonor(OrganDonorStatus.SIM)
                .familyHistory("Hipertensão na família")
                .surgicalHistory("Apendicectomia")
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
        return medicalRecords
                .stream()
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
}
