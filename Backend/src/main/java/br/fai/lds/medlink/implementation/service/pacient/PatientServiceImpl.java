package br.fai.lds.medlink.implementation.service.pacient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public int create(Patient entity) {
        patientDao.create(entity);
        return entity.getId();
    }

    @Override
    public boolean deactivate(int id) {
        return patientDao.deactivate(id);
    }

    @Override
    public boolean delete(int id) {
        return patientDao.remove(id);
    }

    @Override
    public Patient findById(int id) {
        return patientDao.readById(id);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.readAll();
    }

    @Override
    public Patient update(int id, Patient entity) {
        // Implementação futura, se necessário
        throw new UnsupportedOperationException("Update not implemented yet.");
    }

    @Override
    public List<PatientResponseDto> getPatientsByMedicId(int medicId) {
        List<Patient> patients = patientDao.findByMedicId(medicId);
        return patients.stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByMedicId(int medicId) {
        return patientDao.findByMedicId(medicId);
    }
}
