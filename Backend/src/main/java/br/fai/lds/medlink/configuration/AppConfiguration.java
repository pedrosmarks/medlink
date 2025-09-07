package br.fai.lds.medlink.configuration;

import br.fai.lds.medlink.implementation.dao.postgres.MedicPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MedicalRecordPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MessagePostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.PatientPostgresDaoImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import br.fai.lds.medlink.port.dao.message.MessageDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.util.Arrays;

@Configuration
public class AppConfiguration {


    //Environment é usada para  acessar propriedades e perfis ativos do Spring.
    private final Environment environment;

    public AppConfiguration(Environment environment) {
        this.environment = environment;

        System.out.println("----");
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        System.out.println("-----");

    }

    @Bean
    public PatientDao getPatientDao(final Connection connection) {
        // return new PatientFakeDaoImpl(); // DAO Fake (comentado)
        return new PatientPostgresDaoImpl(connection); // DAO PostgreSQL
    }

    @Bean
    public MedicDao getMedicDao(final Connection connection) {
        // return new MedicFakeDaoImpl(); // DAO Fake (comentado)
        return new MedicPostgresDaoImpl(connection); // DAO PostgreSQL
    }

    @Bean
    public MedicalRecordDao getMedicalRecordDao(final Connection connection) {
        // return new MedicalRecordFakeDaoImpl(); // DAO Fake (comentado)
        return new MedicalRecordPostgresDaoImpl(connection); // DAO PostgreSQL
    }

    @Bean
    public MessageDao getMessageDao(final Connection connection) {
        return new MessagePostgresDaoImpl(connection); // DAO PostgreSQL
    }
}