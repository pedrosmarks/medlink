package br.fai.lds.medlink.configuration;

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
}
