package br.fai.lds.medlink.implementation.service.medicalRecord;

import br.fai.lds.medlink.domain.MedicalRecord;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.MedicalRecordResponseDto;
import br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.clinical.*;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import br.fai.lds.medlink.port.service.medicalRecordService.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public MedicalRecord update(int id, MedicalRecord entity) {
        MedicalRecord existing = medicalRecordDao.readById(id);
        if (existing == null) {
            return null;
        }
        entity.setId(id);
        medicalRecordDao.updateInformation(id, entity);
        return entity;
    }

    @Override
    public MedicalRecordResponseDto findByPatientId(int medicId, int patientId) {

        MedicalRecord record = medicalRecordDao.findByPatientId(patientId);

        if (record == null) {
            throw new RuntimeException("Prontuário não encontrado para o paciente ID " + patientId);
        }

        return MedicalRecordResponseDto.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .bloodType(record.getBloodType()) // enum BloodType direto
                .organDonor(record.getOrganDonor()) // enum OrganDonorStatus direto
                .diagnosis(record.getDiagnosis())
                .familyHistory(record.getFamilyHistory())
                .alergias(List.of(
                        AlergiaDto.builder().substancia("Amoxicilina").reacao("Erupção cutânea").build()
                ))
                .medicamentos(List.of(
                        MedicamentoDto.builder().nome("Losartana").dosagem("50mg").frequencia("1x ao dia").build()
                ))
                .cirurgias(List.of(
                        CirurgiaDto.builder().nome("Apendicectomia").data(LocalDate.of(2020, 3, 10)).local(null).build()
                ))
                .vacinas(List.of(
                        VacinaDto.builder().nome("Hepatite B").data(LocalDate.of(2019, 7, 21)).build()
                ))
                .consultas(List.of(
                        ConsultaDto.builder().data(LocalDate.of(2024, 5, 1)).motivo("Check-up").observacoes("Tudo normal").build()
                ))
                .medicalRecordActive(record.isMedicalRecordActive())
                .build();
    }
}
