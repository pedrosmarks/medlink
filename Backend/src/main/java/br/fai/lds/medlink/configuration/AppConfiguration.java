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

/**
 * Classe de configuração principal da aplicação MedLink.
 * 
 * <p>Centraliza todas as configurações de beans, DAOs, documentação Swagger
 * e configurações CORS para o sistema médico.</p>
 *
 */
@Configuration
public class AppConfiguration {


    /** Environment para acessar propriedades e perfis ativos do Spring. */
    private final Environment environment;

    /**
     * Construtor da configuração da aplicação.
     * 
     * @param environment instância do Environment do Spring
     */
    public AppConfiguration(Environment environment) {
        this.environment = environment;
    }

    /**
     * Configura o bean do DAO de pacientes.
     * 
     * @param connection conexão com o banco de dados
     * @return instância do PatientDao
     */
    @Bean
    public PatientDao getPatientDao(final Connection connection) {
        return new PatientPostgresDaoImpl(connection);
    }

    /**
     * Configura o bean do DAO de médicos.
     * 
     * @param connection conexão com o banco de dados
     * @return instância do MedicDao
     */
    @Bean
    public MedicDao getMedicDao(final Connection connection) {
        return new MedicPostgresDaoImpl(connection);
    }

    /**
     * Configura o bean do DAO de prontuários médicos.
     * 
     * @param connection conexão com o banco de dados
     * @return instância do MedicalRecordDao
     */
    @Bean
    public MedicalRecordDao getMedicalRecordDao(final Connection connection) {
        return new MedicalRecordPostgresDaoImpl(connection);
    }
    /**
     * Configura a documentação Swagger/OpenAPI da aplicação.
     * 
     * <p>Para acessar a documentação, acesse: http://localhost:8080/swagger-ui.html</p>
     * 
     * @return configuração do OpenAPI para documentação da API
     */
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info()
                .title("MEDLINK")
                .version("1.0.0")
                .description("API do Sistema de Gestão Médica MedLink"));
    }



    /**
     * Configura o bean do DAO de mensagens.
     * 
     * @param connection conexão com o banco de dados
     * @return instância do MessageDao
     */
    @Bean
    public MessageDao getMessageDao(final Connection connection) {
        return new MessagePostgresDaoImpl(connection);
    }

    /**
     * Configuração CORS para permitir requisições do frontend.
     * 
     * <p>Permite requisições de múltiplas origens (React, Angular) e
     * configura recursos estáticos da aplicação.</p>
     * 
     * @return configurador CORS personalizado
     */
    @Bean
    public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
        return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }

            @Override
            public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
                registry.setOrder(1);
            }
        };
    }
}