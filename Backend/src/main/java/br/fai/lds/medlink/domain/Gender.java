package br.fai.lds.medlink.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enumeração que representa os gêneros disponíveis no sistema.
 * 
 * <p>Suporta serialização/deserialização JSON com valores em português e inglês
 * para compatibilidade com diferentes frontends.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum Gender {

    /** Gênero feminino. */
    FEMININO("Feminino"),
    
    /** Gênero masculino. */
    MASCULINO("Masculino"),
    
    /** Outros gêneros ou não especificado. */
    OUTRO("Outro");

    /** Rótulo descritivo do gênero. */
    private final String label;

    /**
     * Construtor do enum com o rótulo do gênero.
     * 
     * @param label descrição do gênero
     */
    Gender(String label) {
        this.label = label;
    }

    /**
     * Retorna a representação textual do gênero para JSON.
     * 
     * @return rótulo do gênero
     */
    @JsonValue
    @Override
    public String toString() {
        return label;
    }

    /**
     * Cria uma instância de Gender a partir de string.
     * Suporta valores em português e inglês.
     * 
     * @param value valor string do gênero
     * @return instância correspondente de Gender
     * @throws IllegalArgumentException se o valor não for reconhecido
     */
    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }

        String upperValue = value.toUpperCase().trim();

        // Aceita valores em português
        switch (upperValue) {
            case "FEMININO":
            case "F":
                return FEMININO;
            case "MASCULINO":
            case "M":
                return MASCULINO;
            case "OUTRO":
            case "O":
                return OUTRO;
        }

        // Aceita valores em inglês
        switch (upperValue) {
            case "FEMALE":
                return FEMININO;
            case "MALE":
                return MASCULINO;
            case "OTHER":
                return OUTRO;
        }

        throw new IllegalArgumentException("Valor inválido para Gender: " + value +
            ". Valores aceitos: FEMININO/FEMALE, MASCULINO/MALE, OUTRO/OTHER");
    }
}
