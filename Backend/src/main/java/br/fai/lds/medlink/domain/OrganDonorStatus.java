package br.fai.lds.medlink.domain;

import lombok.Getter;

@Getter
public enum OrganDonorStatus {

    SIM("Sim"),
    NAO("Não");

    private final String label;

    OrganDonorStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
