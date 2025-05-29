package br.fai.lds.medlink.domain;

import lombok.Getter;

@Getter
public enum BloodType {

    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("0+"),
    O_NEGATIVE("0-");

    //responsavel por acessar o texto tratado fora da enum
    //Declaracao de variavel de instancia label
    private final String label;

    //Passa como parametro o texto ja tratado e atribuido a varial label
    BloodType(String label) {
        this.label = label;
    }

    // sobrescreve o metodo para retornar o valor já tratado
    @Override
    public String toString() {
        return label;
    }
}
