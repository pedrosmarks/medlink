package br.fai.lds.medlink.domain;

import lombok.Getter;

/**
 * Enumeração que representa os tipos sanguíneos no sistema ABO/Rh.
 * 
 * <p>Contém todos os tipos sanguíneos possíveis com suas representações
 * padronizadas para uso médico.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum BloodType {

    /** Tipo sanguíneo A positivo. */
    A_POSITIVE("A+"),
    
    /** Tipo sanguíneo A negativo. */
    A_NEGATIVE("A-"),
    
    /** Tipo sanguíneo B positivo. */
    B_POSITIVE("B+"),
    
    /** Tipo sanguíneo B negativo. */
    B_NEGATIVE("B-"),
    
    /** Tipo sanguíneo AB positivo. */
    AB_POSITIVE("AB+"),
    
    /** Tipo sanguíneo AB negativo. */
    AB_NEGATIVE("AB-"),
    
    /** Tipo sanguíneo O positivo. */
    O_POSITIVE("O+"),
    
    /** Tipo sanguíneo O negativo. */
    O_NEGATIVE("O-");

    /** Representação textual do tipo sanguíneo. */
    private final String label;

    /**
     * Construtor do enum com o rótulo do tipo sanguíneo.
     * 
     * @param label representação textual do tipo sanguíneo
     */
    BloodType(String label) {
        this.label = label;
    }

    /**
     * Retorna a representação textual do tipo sanguíneo.
     * 
     * @return rótulo do tipo sanguíneo (ex: "A+", "O-")
     */
    @Override
    public String toString() {
        return label;
    }
}
