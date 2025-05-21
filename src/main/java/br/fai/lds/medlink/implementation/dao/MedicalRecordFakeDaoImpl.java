package br.fai.lds.medlink.implementation.dao;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;

import java.util.List;

public class MedicalRecordFakeDaoImpl implements MedicalRecordDao {
    @Override
    public void create(MedicalRecord entity) {

    }

    @Override
    public boolean remove(int id) {
        return false;
    }

    @Override
    public MedicalRecord readById(int id) {
        return null;
    }

    @Override
    public List<MedicalRecord> readAll() {
        return List.of();
    }

    @Override
    public void updateInformation(int id, MedicalRecord entity) {

    }
}
