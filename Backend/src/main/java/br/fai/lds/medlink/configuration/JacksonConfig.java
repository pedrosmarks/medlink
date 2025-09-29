package br.fai.lds.medlink.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Configuração do Jackson para serialização/deserialização JSON.
 * 
 * <p>Personaliza o ObjectMapper para suportar múltiplos formatos de data,
 * permitindo compatibilidade com diferentes frontends (ISO e formato brasileiro).</p>
 *
 */
@Configuration
public class JacksonConfig {

    /**
     * Configura o ObjectMapper principal da aplicação.
     * 
     * <p>Suporta deserialização de datas nos formatos:
     * <ul>
     *   <li>ISO (yyyy-MM-dd)</li>
     *   <li>Brasileiro (dd/MM/yyyy)</li>
     * </ul>
     * E serialização no formato ISO padrão.</p>
     * 
     * @return ObjectMapper configurado para o sistema
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        LocalDateDeserializer customDeserializer = new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE) {
            @Override
            protected LocalDate _fromString(com.fasterxml.jackson.core.JsonParser p, 
                    com.fasterxml.jackson.databind.DeserializationContext ctxt, String string0) throws java.io.IOException {
                try {
                    return LocalDate.parse(string0, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e1) {
                    try {
                        return LocalDate.parse(string0, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    } catch (Exception e2) {
                        return super._fromString(p, ctxt, string0);
                    }
                }
            }
        };
        
        javaTimeModule.addDeserializer(LocalDate.class, customDeserializer);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}