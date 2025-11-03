package br.fai.lds.medlink.implementation.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.PatientAuthorizedDto;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.medic.MedicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MedicServiceImpl implements MedicService {

    private final MedicDao medicDao;
    private final PatientDao patientDao;

    @Autowired
    public MedicServiceImpl(MedicDao medicDao, PatientDao patientDao) {
        this.medicDao = medicDao;
        this.patientDao = patientDao;
    }

    @Override
    public int create(Medic entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Médico não pode ser nulo");
        }
        try {
            medicDao.create(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar médico", e);
        }
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        try {
            Medic medic = medicDao.readById(id);
            if (medic == null) {
                return false;
            }
            medic.setActive(false);
            medicDao.updateInformation(id, medic);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar médico", e);
        }
    }

    @Override
    public Medic findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        try {
            return medicDao.readById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médico", e);
        }
    }

    @Override
    public List<Medic> findAll() {
        try {
            return medicDao.readAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médicos", e);
        }
    }

    @Override
    public Medic update(int id, Medic entity) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Médico não pode ser nulo");
        }
        
        try {
            Medic existingMedic = medicDao.readById(id);
            if (existingMedic == null) {
                throw new IllegalArgumentException("Médico com o id " + id + " não foi encontrado");
            }

            existingMedic.setName(entity.getName());
            existingMedic.setCpf(entity.getCpf());
            existingMedic.setGender(entity.getGender());
            existingMedic.setBirthDate(entity.getBirthDate());
            existingMedic.setPhoneNumber(entity.getPhoneNumber());
            existingMedic.setAddress(entity.getAddress());
            existingMedic.setCrm(entity.getCrm());
            existingMedic.setSpecialty(entity.getSpecialty());
            existingMedic.setEmail(entity.getEmail());
            existingMedic.setActive(entity.isActive());

            medicDao.updateInformation(id, existingMedic);
            return existingMedic;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar médico", e);
        }
    }

    @Override
    public Map<Integer, Medic> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            Map<Integer, Medic> medicsMap = new HashMap<>();
            for (Integer id : ids) {
                if (id != null && id > 0) {
                    Medic medic = medicDao.readById(id);
                    if (medic != null) {
                        medicsMap.put(id, medic);
                    }
                }
            }
            return medicsMap;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médicos por IDs", e);
        }
    }

    @Override
    public List<PatientAuthorizedDto> findAuthorizedPatients(int medicId) {
        List<Patient> patients = patientDao.findAuthorizedByMedicId(medicId);
        List<PatientAuthorizedDto> dtos = new java.util.ArrayList<>();
        for (Patient p : patients) {
            dtos.add(new PatientAuthorizedDto(p.getId(), p.getName(), p.getBirthDate(), p.getEmail()));
        }
        return dtos;
    }
}