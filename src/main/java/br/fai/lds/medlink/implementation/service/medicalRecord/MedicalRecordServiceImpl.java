package br.fai.lds.medlink.implementation.service.medicalRecord;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {
    @Override
    public MedicalRecord readById(int id) {
        return null;
    }

    @Override
    public int create(MedicalRecord entity) {
        return 0;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public MedicalRecord findById(int id) {
        return null;
    }

    @Override
    public List<MedicalRecord> findAll() {
        return List.of();
    }

    @Override
    public MedicalRecord update(int id, MedicalRecord entity) {
        return null;
    }
}
