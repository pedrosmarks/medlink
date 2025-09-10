package br.fai.lds.medlink.implementation.service.medic;

import br.fai.lds.medlink.domain.Medic;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.service.medic.MedicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.fai.lds.medlink.util.LogSanitizer;

@Slf4j
@Service
public class MedicServiceImpl implements MedicService {

    private final MedicDao medicDao;

    @Autowired
    public MedicServiceImpl(MedicDao medicDao) {
        this.medicDao = medicDao;
    }

    @Override
    public int create(Medic entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Médico não pode ser nulo");
        }
        try {
            medicDao.create(entity);
            log.info("Médico criado com sucesso: {}", LogSanitizer.sanitizeAndLimit(entity.getName(), 50));
            return entity.getId();
        } catch (Exception e) {
            log.error("Erro ao criar médico: {}", LogSanitizer.sanitize(e.getMessage()), e);
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
                log.warn("Tentativa de deletar médico inexistente com ID: {}", LogSanitizer.sanitizeId(id));
                return false;
            }
            medic.setActive(false);
            medicDao.updateInformation(id, medic);
            log.info("Médico desativado com sucesso: {} (ID: {})", LogSanitizer.sanitizeAndLimit(medic.getName(), 50), LogSanitizer.sanitizeId(id));
            return true;
        } catch (Exception e) {
            log.error("Erro ao deletar médico ID {}: {}", LogSanitizer.sanitizeId(id), LogSanitizer.sanitize(e.getMessage()), e);
            throw new RuntimeException("Erro ao deletar médico", e);
        }
    }

    @Override
    public Medic findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        try {
            Medic medic = medicDao.readById(id);
            if (medic == null) {
                log.warn("Médico não encontrado com ID: {}", LogSanitizer.sanitizeId(id));
            }
            return medic;
        } catch (Exception e) {
            log.error("Erro ao buscar médico ID {}: {}", LogSanitizer.sanitizeId(id), LogSanitizer.sanitize(e.getMessage()), e);
            throw new RuntimeException("Erro ao buscar médico", e);
        }
    }

    @Override
    public List<Medic> findAll() {
        try {
            List<Medic> medics = medicDao.readAll();
            log.debug("Encontrados {} médicos", medics.size());
            return medics;
        } catch (Exception e) {
            log.error("Erro ao buscar todos os médicos: {}", LogSanitizer.sanitize(e.getMessage()), e);
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
            existingMedic.setGender(entity.getGender());
            existingMedic.setBirthDate(entity.getBirthDate());
            existingMedic.setPhoneNumber(entity.getPhoneNumber());
            existingMedic.setAddress(entity.getAddress());
            existingMedic.setCrm(entity.getCrm());
            existingMedic.setSpecialty(entity.getSpecialty());
            existingMedic.setEmail(entity.getEmail());
            existingMedic.setActive(entity.isActive());

            medicDao.updateInformation(id, existingMedic);
            log.info("Médico atualizado com sucesso: {} (ID: {})", LogSanitizer.sanitizeAndLimit(existingMedic.getName(), 50), LogSanitizer.sanitizeId(id));
            
            return existingMedic;
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar médico ID {}: {}", LogSanitizer.sanitizeId(id), LogSanitizer.sanitize(e.getMessage()));
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar médico ID {}: {}", LogSanitizer.sanitizeId(id), LogSanitizer.sanitize(e.getMessage()), e);
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
            log.debug("Encontrados {} médicos de {} IDs solicitados", medicsMap.size(), ids.size());
            return medicsMap;
        } catch (Exception e) {
            log.error("Erro ao buscar médicos por IDs {}: {}", ids.toString(), LogSanitizer.sanitize(e.getMessage()), e);
            throw new RuntimeException("Erro ao buscar médicos por IDs", e);
        }
    }
}
