package br.fai.lds.medlink.domain;

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

    @Override
    public String toString() {
        return label;
    }
}
