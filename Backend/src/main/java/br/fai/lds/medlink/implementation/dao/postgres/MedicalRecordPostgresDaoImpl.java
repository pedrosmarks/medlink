package br.fai.lds.medlink.implementation.dao.postgres;

import br.fai.lds.medlink.domain.*;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MedicalRecordPostgresDaoImpl implements MedicalRecordDao {

    private final Connection connection;

    public MedicalRecordPostgresDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(final MedicalRecord entity) {
        String sql = "INSERT INTO prontuario(paciente_id, tipo_sanguineo, doador_orgao, diagnostico, historico_familiar) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, entity.getPatientId());
            preparedStatement.setString(2, entity.getBloodType() != null ? entity.getBloodType().toString() : "O+");
            preparedStatement.setBoolean(3, entity.getOrganDonor() == OrganDonorStatus.SIM);
            preparedStatement.setString(4, entity.getDiagnosis());
            preparedStatement.setString(5, entity.getFamilyHistory());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM prontuario WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MedicalRecord readById(int id) {
        final String sql = """
            SELECT pr.id, pr.paciente_id, pr.tipo_sanguineo, pr.doador_orgao,
                   pr.observacoes, pr.historico_familiar
            FROM prontuario pr
            WHERE pr.id = ?
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MedicalRecord medicalRecord = buildMedicalRecordFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medicalRecord;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<MedicalRecord> readAll() {
        final String sql = """
            SELECT pr.id, pr.paciente_id, pr.tipo_sanguineo, pr.doador_orgao,
                   pr.observacoes, pr.historico_familiar
            FROM prontuario pr
        """;

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                medicalRecords.add(buildMedicalRecordFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    public void update(MedicalRecord entity) {
        String sql = "UPDATE prontuario SET tipo_sanguineo = ?, doador_orgao = ?, diagnostico = ?, historico_familiar = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getBloodType() != null ? entity.getBloodType().toString() : "O+");
            preparedStatement.setBoolean(2, entity.getOrganDonor() == OrganDonorStatus.SIM);
            preparedStatement.setString(3, entity.getDiagnosis());
            preparedStatement.setString(4, entity.getFamilyHistory());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInformation(int id, MedicalRecord entity) {
        String sql = "UPDATE prontuario SET tipo_sanguineo = ?, doador_orgao = ?, diagnostico = ?, historico_familiar = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getBloodType() != null ? entity.getBloodType().toString() : "O+");
            preparedStatement.setBoolean(2, entity.getOrganDonor() == OrganDonorStatus.SIM);
            preparedStatement.setString(3, entity.getDiagnosis());
            preparedStatement.setString(4, entity.getFamilyHistory());
            preparedStatement.setInt(5, id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MedicalRecord findByPatientId(int patientId) {
        final String sql = """
            SELECT pr.id, pr.paciente_id, pr.tipo_sanguineo, pr.doador_orgao,
                   pr.observacoes, pr.historico_familiar
            FROM prontuario pr
            WHERE pr.paciente_id = ?
        """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, patientId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MedicalRecord medicalRecord = buildMedicalRecordFromResultSet(resultSet);
                preparedStatement.close();
                resultSet.close();
                return medicalRecord;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<MedicalRecord> findByMedicId(int medicId) {
        final String sql = """
            SELECT DISTINCT pr.id, pr.paciente_id, pr.tipo_sanguineo, pr.doador_orgao,
                   pr.observacoes, pr.historico_familiar
            FROM prontuario pr
            JOIN medico_acesso_prontuario map ON pr.id = map.prontuario_id
            WHERE map.medico_id = ?
        """;

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, medicId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                medicalRecords.add(buildMedicalRecordFromResultSet(resultSet));
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    private MedicalRecord buildMedicalRecordFromResultSet(ResultSet resultSet) throws SQLException {
        List<Medication> medications = new ArrayList<>();
        List<Allergy> allergies = new ArrayList<>();
        List<Vaccine> vaccines = new ArrayList<>();
        List<Surgery> surgeries = new ArrayList<>();
        List<Consultation> consultations = new ArrayList<>();

        return MedicalRecord.builder()
                .id(resultSet.getInt("id"))
                .patientId(resultSet.getInt("paciente_id"))
                .bloodType(parseBloodType(resultSet.getString("tipo_sanguineo")))
                .organDonor(resultSet.getBoolean("doador_orgao") ? OrganDonorStatus.SIM : OrganDonorStatus.NAO)
                .diagnosis(resultSet.getString("diagnostico"))
                .familyHistory(resultSet.getString("historico_familiar"))
                .medications(medications)
                .allergies(allergies)
                .vaccines(vaccines)
                .surgeries(surgeries)
                .consultations(consultations)
                .build();
    }

    private BloodType parseBloodType(String bloodTypeStr) {
        if (bloodTypeStr == null) return BloodType.O_POSITIVE;
        try {
            return switch (bloodTypeStr.toUpperCase()) {
                case "A+" -> BloodType.A_POSITIVE;
                case "A-" -> BloodType.A_NEGATIVE;
                case "B+" -> BloodType.B_POSITIVE;
                case "B-" -> BloodType.B_NEGATIVE;
                case "AB+" -> BloodType.AB_POSITIVE;
                case "AB-" -> BloodType.AB_NEGATIVE;
                case "O+" -> BloodType.O_POSITIVE;
                case "O-" -> BloodType.O_NEGATIVE;
                default -> BloodType.O_POSITIVE;
            };
        } catch (Exception e) {
            return BloodType.O_POSITIVE;
        }
    }
}