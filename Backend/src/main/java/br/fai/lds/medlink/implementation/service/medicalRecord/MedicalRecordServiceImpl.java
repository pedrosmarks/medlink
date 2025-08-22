package br.fai.lds.medlink.implementation.service.medicalRecord;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical.*;
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
        return medicalRecordDao.readById(id);
    }

    @Override
    public int create(MedicalRecord entity) {
        medicalRecordDao.create(entity);
        return entity.getId();
    }

    @Override
    public boolean delete(int id) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.setMedicalRecordActive(false);
            medicalRecordDao.updateInformation(id, record);
            return true;
        }
        return false;
    }

    @Override
    public MedicalRecord findById(int id) {
        return medicalRecordDao.readById(id);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordDao.readAll();
    }

    @Override
    public MedicalRecord update(int id, MedicalRecordUpdateDto dto) {
        MedicalRecord existing = medicalRecordDao.readById(id);
        if (existing == null) {
            return null;
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
        MedicalRecord record = medicalRecordDao.findByPatientId(patientId);
        if (record == null) {
            throw new RuntimeException("Medical record not found for patient ID " + patientId);
        }

        return MedicalRecordResponseDto.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .bloodType(record.getBloodType())
                .organDonor(record.getOrganDonor())
                .diagnosis(record.getDiagnosis())
                .familyHistory(record.getFamilyHistory())
                .allergies(
                        record.getAllergies() == null ? List.of() :
                                record.getAllergies().stream()
                                        .map(allergy -> AllergyCreateDto.builder()
                                                .name(allergy.getName())
                                                .substance(allergy.getSubstance())
                                                .reaction(allergy.getReaction())
                                                .severity(allergy.getSeverity())
                                                .build())
                                        .collect(Collectors.toList())
                )
                .medications(
                record.getMedications() == null ? List.of() :
                        record.getMedications().stream()
                                .map(medication -> MedicationCreateDto.builder()
                                        .name(medication.getName())
                                        .dosage(medication.getDosage())
                                        .frequency(medication.getFrequency())
                                        .build())
                                .collect(Collectors.toList())
        )

                .surgeries(
                record.getSurgeries() == null ? List.of() :
                        record.getSurgeries().stream()
                                .map(surgery -> SurgeryCreateDto.builder()
                                        .name(surgery.getName())
                                        .date(surgery.getDate())
                                        .location(surgery.getLocation())
                                        .notes(surgery.getNotes())
                                        .build())
                                .collect(Collectors.toList())
        )
                .vaccines(
                record.getVaccines() == null ? List.of() :
                        record.getVaccines().stream()
                                .map(vaccine -> VaccineCreateDto.builder()
                                        .name(vaccine.getName())
                                        .date(vaccine.getDate())
                                        .build())
                                .collect(Collectors.toList())
        )

                .consultations(
                        record.getConsultations() == null ? List.of() :
                                record.getConsultations().stream()
                                        .map(consult -> ConsultationCreateDto.builder()
                                                .date(consult.getDate())
                                                .reason(consult.getReason())
                                                .notes(consult.getNotes())
                                                .build())
                                        .collect(Collectors.toList())
                )
                .medicalRecordActive(record.isMedicalRecordActive())
                .build();
    }

    @Override
    public void addConsultation(int medicalRecordId, Consultation consultation) {
        MedicalRecord record = medicalRecordDao.readById(medicalRecordId);
        if (record != null) {
            record.getConsultations().add(consultation);
            medicalRecordDao.updateInformation(medicalRecordId, record);
        }
    }


    @Override
    public void addMedication(int id, Medication medication) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.getMedications().add(medication);
            medicalRecordDao.updateInformation(id, record);
        }
    }

    @Override
    public void addAllergy(int id, Allergy allergy) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.getAllergies().add(allergy);
            medicalRecordDao.updateInformation(id, record);
        }
    }

    @Override
    public void addSurgery(int id, Surgery surgery) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.getSurgeries().add(surgery);
            medicalRecordDao.updateInformation(id, record);
        }
    }



    @Override
    public void addVaccine(int id, Vaccine vaccine) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.getVaccines().add(vaccine);
            medicalRecordDao.updateInformation(id, record);
        }
    }

    @Override
    public void addDiagnosis(int id, Diagnosis diagnosis) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            // Como MedicalRecord tem diagnosis como String, vamos usar a descrição do diagnóstico
            record.setDiagnosis(diagnosis.getDescription());
            medicalRecordDao.updateInformation(id, record);
        }
    }

    @Override
    public void addFamilyHistory(int id, String familyHistory) {
        MedicalRecord record = medicalRecordDao.readById(id);
        if (record != null) {
            record.setFamilyHistory(familyHistory);
            medicalRecordDao.updateInformation(id, record);
        }
    }

    @Override
    public MedicalRecord update(int id, MedicalRecord entity) {
        MedicalRecord existing = medicalRecordDao.readById(id);
        if (existing == null) {
            return null;
        }
        medicalRecordDao.updateInformation(id, entity);
        return entity;
    }
}
