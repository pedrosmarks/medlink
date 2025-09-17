package br.fai.lds.medlink.domain;

import lombok.Getter;

/**
 * Enumeração que representa o status de doador de órgãos.
 * 
 * <p>Indica se o paciente é ou não doador de órgãos,
 * informação importante para o prontuário médico.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum OrganDonorStatus {

    /** Paciente é doador de órgãos. */
    SIM("Sim"),
    
    /** Paciente não é doador de órgãos. */
    NAO("Não");

    /** Rótulo descritivo do status. */
    private final String label;

    /**
     * Construtor do enum com o rótulo do status.
     * 
     * @param label descrição do status de doador
     */
    OrganDonorStatus(String label) {
        this.label = label;
    }

    /**
     * Retorna a representação textual do status.
     * 
     * @return rótulo do status ("Sim" ou "Não")
     */
    @Override
    public String toString() {
        return label;
    }
}
