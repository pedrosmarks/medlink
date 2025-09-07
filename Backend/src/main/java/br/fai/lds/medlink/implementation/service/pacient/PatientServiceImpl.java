package br.fai.lds.medlink.implementation.service.pacient;

import br.fai.lds.medlink.domain.Patient;
import br.fai.lds.medlink.domain.dataTransferObject.Patient.PatientResponseDto;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.patient.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {
    @Override
    public void updateAccessRequestStatus(int patientId, int medicoId, String status) {
        patientDao.updateAccessRequestStatus(patientId, medicoId, status);
    }
    @Override
    public void authorizeSpecialist(int patientId, int medicoId) {
        patientDao.authorizeSpecialist(patientId, medicoId);
    }

    @Autowired
    private PatientDao patientDao;

    @Override
    public int create(Patient entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Paciente não pode ser nulo");
        }
        try {
            patientDao.create(entity);
            log.info("Paciente criado com sucesso: {}", entity.getName());
            return entity.getId();
        } catch (Exception e) {
            log.error("Erro ao criar paciente: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar paciente", e);
        }
    }

    @Override
    public boolean remove(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        try {
            boolean result = patientDao.remove(id);
            if (result) {
                log.info("Paciente removido com sucesso: ID {}", id);
            } else {
                log.warn("Tentativa de remover paciente inexistente: ID {}", id);
            }
            return result;
        } catch (Exception e) {
            log.error("Erro ao remover paciente ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erro ao remover paciente", e);
        }
    }

    @Override
    public Patient findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        try {
            Patient patient = patientDao.readById(id);
            if (patient == null) {
                log.warn("Paciente não encontrado com ID: {}", id);
            }
            return patient;
        } catch (Exception e) {
            log.error("Erro ao buscar paciente ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar paciente", e);
        }
    }

    @Override
    public Patient findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        try {
            Patient patient = patientDao.findByEmail(email);
            if (patient == null) {
                log.warn("Paciente não encontrado com email: {}", email);
            }
            return patient;
        } catch (Exception e) {
            log.error("Erro ao buscar paciente por email {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar paciente por email", e);
        }
    }

    @Override
    public List<Patient> findAll() {
        try {
            List<Patient> patients = patientDao.findAll();
            log.debug("Encontrados {} pacientes", patients.size());
            return patients;
        } catch (Exception e) {
            log.error("Erro ao buscar todos os pacientes: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar pacientes", e);
        }
    }

    @Override
    public boolean updateInformation(int id, Patient entity) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Paciente não pode ser nulo");
        }
        if (patientDao.readById(id) == null) {
            throw new IllegalArgumentException("Paciente com ID " + id + " não encontrado");
        }
        try {
            patientDao.updateInformation(id, entity);
            return true;
        } catch (Exception e) {
            log.error("Erro ao atualizar informações do paciente ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deactivate(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (patientDao.readById(id) == null) {
            throw new IllegalArgumentException("Paciente com ID " + id + " não encontrado");
        }
        return patientDao.deactivate(id);
    }

    @Override
    public boolean delete(int id) {
    throw new UnsupportedOperationException("Delete não implementado");
    }

    @Override
    public List<PatientResponseDto> getPatientsByMedicId(int medicId) {
        List<Patient> patients = patientDao.findByMedicId(medicId);
        List<PatientResponseDto> dtos = new java.util.ArrayList<>();
        for (Patient p : patients) {
            dtos.add(PatientResponseDto.fromEntity(p));
        }
        return dtos;
    }

    @Override
    public List<Patient> findByMedicId(int medicId) {
        if (medicId <= 0) {
            throw new IllegalArgumentException("ID do médico deve ser maior que zero");
        }
        return patientDao.findByMedicId(medicId);
    }

    // Implementação do método update
    @Override
    public Patient update(int id, Patient entity) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Paciente não pode ser nulo");
        }
        
        try {
            Patient existing = patientDao.readById(id);
            if (existing == null) {
                throw new IllegalArgumentException("Paciente com ID " + id + " não encontrado");
            }
            
            entity.setId(id); // Garantir que o ID seja mantido
            patientDao.updateInformation(id, entity);
            log.info("Paciente atualizado com sucesso: {} (ID: {})", entity.getName(), id);
            return patientDao.readById(id);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar paciente ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar paciente ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
    }

    @Override
    public void sendAccessRequest(int patientId, int medicoId) {
        patientDao.createAccessRequest(patientId, medicoId);
    }
}