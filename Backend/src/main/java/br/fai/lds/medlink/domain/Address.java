package br.fai.lds.medlink.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa o endereço de uma pessoa no sistema.
 * 
 * <p>Contém informações completas de localização incluindo validações
 * para formato de CEP brasileiro.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    /** Nome da rua ou logradouro. */
    @NotBlank(message = "Nome da rua não pode estar em branco")
    private String street;

    /** Número do endereço. */
    @Size(min = 1, message = "Numero deve ter pelo menos 1 caractere")
    private String number;

    /** Complemento do endereço (apartamento, bloco, etc.). */
    private String complement;

    /** Nome do bairro. */
    @NotBlank(message = "Bairro não pode estar em branco")
    private String neighborhood;

    /** Nome da cidade. */
    @NotBlank(message = "Cidade não pode estar em branco")
    private String city;

    /** Nome do estado. */
    @NotBlank(message = "Estado não pode estar em branco")
    private String state;

    /** Código de Endereçamento Postal no formato XXXXX-XXX. */
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP deve estar no formato XXXXX-XXX")
    private String zipCode;

}