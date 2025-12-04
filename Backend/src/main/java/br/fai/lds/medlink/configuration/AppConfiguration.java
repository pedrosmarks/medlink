package br.fai.lds.medlink.configuration;

import br.fai.lds.medlink.implementation.dao.postgres.MedicPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MedicalRecordPostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.MessagePostgresDaoImpl;
import br.fai.lds.medlink.implementation.dao.postgres.PatientPostgresDaoImpl;
import br.fai.lds.medlink.implementation.service.authentication.AuthenticationServiceImpl;
import br.fai.lds.medlink.implementation.service.authentication.JwtAuthenticationServiceImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.medicalRecord.MedicalRecordDao;
import br.fai.lds.medlink.port.dao.message.MessageDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import br.fai.lds.medlink.port.service.authentication.AuthenticationService;
import br.fai.lds.medlink.port.service.medic.MedicService;
import br.fai.lds.medlink.port.service.patient.PatientService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;

@Configuration
public class AppConfiguration {

    private final Environment environment;
    public AppConfiguration(Environment environment) {
        this.environment = environment;
        System.out.println("------");
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        System.out.println("------");
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
        return new OpenAPI().info(new Info()
                .title("MEDLINK")
                .version("1.0.0")
                .description("API do Sistema de Gestão Médica MedLink"));
    }

    @Bean
    @Profile("basic")
    public AuthenticationService basicAuthenticationService(
            final MedicService medicService,
            final PatientService patientService){
        return new AuthenticationServiceImpl(medicService, patientService);
    }

    @Bean
    @Profile("jwt")
    public AuthenticationService jwtAuthenticationService(
            final MedicService medicService,
            final PatientService patientService,
            final PasswordEncoder passwordEncoder){
        return new JwtAuthenticationServiceImpl(medicService, patientService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Bean
    public MessageDao getMessageDao(final Connection connection) {
        return new MessagePostgresDaoImpl(connection);
    }


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