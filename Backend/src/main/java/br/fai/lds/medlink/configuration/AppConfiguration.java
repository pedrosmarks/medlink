package br.fai.lds.medlink.configuration;

import br.fai.lds.medlink.implementation.dao.postgres.MedicPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MedicalRecordPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MessagePostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.PatientPostgresDaoImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import br.fai.lds.medlink.port.dao.message.MessageDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
        return new PatientPostgresDaoImpl(connection);
    }

    @Bean
    public MedicDao getMedicDao(final Connection connection) {
        return new MedicPostgresDaoImpl(connection);
    }

    @Bean
    public MedicalRecordDao getMedicalRecordDao(final Connection connection) {
        return new MedicalRecordPostgresDaoImpl(connection);
    }
    @Bean
    public OpenAPI customOpenApi(){
        /**
         * Para acessar a URL do swagger pelo navegador, basta
         * digitar localhost:8080/swagger-ui.html
         * Lembrando que : 8080 é a porta, caso você tenha alterado no
         * arquivo application.properties, altere aqui também.
         * @retun;
         */
        return new OpenAPI().info(new Info().title("MEDLINK").version("0.0.1").description("API - MEDLINK"));
}



    @Bean
    public MessageDao getMessageDao(final Connection connection) {
        return new MessagePostgresDaoImpl(connection);
    }
}