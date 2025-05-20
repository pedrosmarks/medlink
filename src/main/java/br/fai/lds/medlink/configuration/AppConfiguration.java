package br.fai.lds.medlink.configuration;

import br.fai.lds.medlink.implementation.dao.MedicFakeDaoImpl;
import br.fai.lds.medlink.implementation.dao.PatientFakeDaoImpl;
import br.fai.lds.medlink.port.dao.medic.MedicDao;
import br.fai.lds.medlink.port.dao.patient.PatientDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
    public PatientDao getPatientFakeDao() {
        return new PatientFakeDaoImpl();
    }

    @Bean
    public MedicDao getMedicFakeDao() {
        return new MedicFakeDaoImpl();
    }


}