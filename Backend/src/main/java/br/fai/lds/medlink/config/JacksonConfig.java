package br.fai.lds.medlink.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        // Deserializer customizado para aceitar múltiplos formatos
        LocalDateDeserializer customDeserializer = new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")) {
            @Override
            protected LocalDate _fromString(com.fasterxml.jackson.core.JsonParser p, 
                    com.fasterxml.jackson.databind.DeserializationContext ctxt, String string0) throws java.io.IOException {
                try {
                    return LocalDate.parse(string0, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e1) {
                    try {
                        return LocalDate.parse(string0, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception e2) {
                        return super._fromString(p, ctxt, string0);
                    }
                }
            }
        };
        
        javaTimeModule.addDeserializer(LocalDate.class, customDeserializer);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}