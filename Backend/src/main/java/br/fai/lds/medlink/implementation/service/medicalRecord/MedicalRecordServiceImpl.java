package br.fai.lds.medlink.implementation.service.medicalRecord;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Allergy.AllergyCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Consultation.ConsultationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Medication.MedicationCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Surgery.SurgeryCreateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine.VaccineCreateDto;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordDao medicalRecordDao;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordDao medicalRecordDao) {
        this.medicalRecordDao = medicalRecordDao;
    }

    @Override
    public MedicalRecord readById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        return medicalRecordDao.readById(id);
    }

    @Override
    public int create(MedicalRecord entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Prontuário médico não pode ser nulo");
        }
        medicalRecordDao.create(entity);
        return entity.getId();
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        record.setMedicalRecordActive(false);
        medicalRecordDao.updateInformation(id, record);
        return true;
    }

    @Override
    public MedicalRecord findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        return medicalRecordDao.readById(id);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordDao.readAll();
    }

    @Override
    public MedicalRecord update(int id, MedicalRecordUpdateDto dto) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (dto == null) {
            throw new IllegalArgumentException("DTO de atualização não pode ser nulo");
        }
        MedicalRecord existing = medicalRecordDao.readById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        updateEntity(existing, dto);
        medicalRecordDao.updateInformation(id, existing);
        return existing;
    }

    private void updateEntity(MedicalRecord entity, MedicalRecordUpdateDto dto) {
        if (dto.getBloodType() != null) {
            entity.setBloodType(dto.getBloodType());
        }
        if (dto.getOrganDonor() != null) {
            entity.setOrganDonor(dto.getOrganDonor());
        }
        if (dto.getDiagnosis() != null) {
            entity.setDiagnosis(dto.getDiagnosis());
        }
        if (dto.getFamilyHistory() != null) {
            entity.setFamilyHistory(dto.getFamilyHistory());
        }
        if (dto.getMedicalRecordActive() != null) {
            entity.setMedicalRecordActive(dto.getMedicalRecordActive());
        }
        if (dto.getAllergies() != null) {
            entity.setAllergies(dto.getAllergies());
        }
        if (dto.getVaccines() != null) {
            entity.setVaccines(dto.getVaccines());
        }
        if (dto.getSurgeries() != null) {
            entity.setSurgeries(dto.getSurgeries());
        }
        if (dto.getMedications() != null) {
            entity.setMedications(dto.getMedications());
        }
        if (dto.getConsultations() != null) {
            entity.setConsultations(dto.getConsultations());
        }
    }

    @Override
    public MedicalRecordResponseDto findByPatientId(int medicId, int patientId) {
        validateIds(medicId, patientId);
        MedicalRecord record = findRecordByPatientId(patientId);
        return buildResponseDto(record);
    }

    private void validateIds(int medicId, int patientId) {
        if (medicId <= 0) {
            throw new IllegalArgumentException("ID do médico deve ser maior que zero");
        }
        if (patientId <= 0) {
            throw new IllegalArgumentException("ID do paciente deve ser maior que zero");
        }
    }

    private MedicalRecord findRecordByPatientId(int patientId) {
        MedicalRecord record = medicalRecordDao.findByPatientId(patientId);
        if (record == null) {
            throw new IllegalArgumentException("Prontuário médico não encontrado para o paciente ID " + patientId);
        }
        return record;
    }

    private MedicalRecordResponseDto buildResponseDto(MedicalRecord record) {
        return MedicalRecordResponseDto.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .bloodType(record.getBloodType())
                .organDonor(record.getOrganDonor())
                .diagnosis(record.getDiagnosis())
                .familyHistory(record.getFamilyHistory())
                .allergies(mapAllergies(record.getAllergies()))
                .medications(mapMedications(record.getMedications()))
                .surgeries(mapSurgeries(record.getSurgeries()))
                .vaccines(mapVaccines(record.getVaccines()))
                .consultations(mapConsultations(record.getConsultations()))
                .medicalRecordActive(record.isMedicalRecordActive())
                .build();
    }

    private List<AllergyCreateDto> mapAllergies(List<Allergy> allergies) {
        return allergies == null ? List.of() : allergies.stream()
                .map(allergy -> AllergyCreateDto.builder()
                        .name(allergy.getName())
                        .substance(allergy.getSubstance())
                        .reaction(allergy.getReaction())
                        .severity(allergy.getSeverity())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MedicationCreateDto> mapMedications(List<Medication> medications) {
        return medications == null ? List.of() : medications.stream()
                .map(medication -> MedicationCreateDto.builder()
                        .name(medication.getName())
                        .dosage(medication.getDosage())
                        .frequency(medication.getFrequency())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SurgeryCreateDto> mapSurgeries(List<Surgery> surgeries) {
        return surgeries == null ? List.of() : surgeries.stream()
                .map(surgery -> SurgeryCreateDto.builder()
                        .name(surgery.getName())
                        .date(surgery.getDate())
                        .location(surgery.getLocation())
                        .notes(surgery.getNotes())
                        .build())
                .collect(Collectors.toList());
    }

    private List<VaccineCreateDto> mapVaccines(List<Vaccine> vaccines) {
        return vaccines == null ? List.of() : vaccines.stream()
                .map(vaccine -> VaccineCreateDto.builder()
                        .name(vaccine.getName())
                        .date(vaccine.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ConsultationCreateDto> mapConsultations(List<Consultation> consultations) {
        return consultations == null ? List.of() : consultations.stream()
                .map(consult -> ConsultationCreateDto.builder()
                        .date(consult.getDate())
                        .reason(consult.getReason())
                        .notes(consult.getNotes())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean addConsultation(int medicalRecordId, Consultation consultation) {
        return addItemToRecord(medicalRecordId, consultation, "Consulta", 
            record -> record.getConsultations().add(consultation));
    }

    @Override
    public boolean addMedication(int id, Medication medication) {
        return addItemToRecord(id, medication, "Medicação", 
            record -> record.getMedications().add(medication));
    }

    @Override
    public boolean addAllergy(int id, Allergy allergy) {
        return addItemToRecord(id, allergy, "Alergia", 
            record -> record.getAllergies().add(allergy));
    }

    @Override
    public boolean addSurgery(int id, Surgery surgery) {
        return addItemToRecord(id, surgery, "Cirurgia", 
            record -> record.getSurgeries().add(surgery));
    }

    @Override
    public boolean addVaccine(int id, Vaccine vaccine) {
        return addItemToRecord(id, vaccine, "Vacina", 
            record -> record.getVaccines().add(vaccine));
    }

    private <T> boolean addItemToRecord(int id, T item, String itemType, 
            java.util.function.Consumer<MedicalRecord> addOperation) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (item == null) {
            throw new IllegalArgumentException(itemType + " não pode ser nula");
        }
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        try {
            addOperation.accept(record);
            medicalRecordDao.updateInformation(id, record);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addDiagnosis(int id, Diagnosis diagnosis) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (diagnosis == null) {
            throw new IllegalArgumentException("Diagnóstico não pode ser nulo");
        }
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        try {
            record.setDiagnosis(diagnosis.getDescription());
            medicalRecordDao.updateInformation(id, record);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addFamilyHistory(int id, String familyHistory) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (familyHistory == null || familyHistory.trim().isEmpty()) {
            throw new IllegalArgumentException("Histórico familiar não pode ser nulo ou vazio");
        }
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        try {
            record.setFamilyHistory(familyHistory);
            medicalRecordDao.updateInformation(id, record);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public MedicalRecord update(int id, MedicalRecord entity) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Prontuário médico não pode ser nulo");
        }
        MedicalRecord existing = medicalRecordDao.readById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Prontuário médico com ID " + id + " não encontrado");
        }
        medicalRecordDao.updateInformation(id, entity);
        return entity;
    }
}
