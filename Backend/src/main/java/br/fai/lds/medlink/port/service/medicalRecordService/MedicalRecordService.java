package br.fai.lds.medlink.port.service.medicalRecordService;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordUpdateDto;
import br.fai.lds.medlink.port.service.crud.CrudService;

public interface MedicalRecordService extends CrudService<MedicalRecord> {



        MedicalRecord readById(int id);

        MedicalRecord update(int id, MedicalRecordUpdateDto dto);

        MedicalRecordResponseDto findByPatientId(int medicId, int patientId);

        boolean addConsultation(int medicalRecordId, Consultation consultation);

        boolean addMedication(int medicalRecordId, Medication medication);

        boolean addAllergy(int medicalRecordId, Allergy allergy);

        boolean addVaccine(int medicalRecordId, Vaccine vaccine);

        boolean addSurgery(int medicalRecordId, Surgery surgery);

        boolean addDiagnosis(int medicalRecordId, Diagnosis diagnosis);

        boolean addFamilyHistory(int id, String familyHistory);
}

