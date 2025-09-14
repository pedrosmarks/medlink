package br.fai.lds.medlink.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Gender {

    FEMININO("Feminino"),
    MASCULINO("Masculino"),
    OUTRO("Outro");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @JsonValue
    @Override
    public String toString() {
        return label;
    }

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
