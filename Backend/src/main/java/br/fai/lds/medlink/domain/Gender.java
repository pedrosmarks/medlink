package br.fai.lds.medlink.domain;

public enum Gender {

    FEMININO("Feminino"),
    MASCULINO("Masculino"),
    OUTRO("Outro");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
