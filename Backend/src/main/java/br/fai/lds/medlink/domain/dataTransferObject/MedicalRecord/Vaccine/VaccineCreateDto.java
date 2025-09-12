package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Vaccine;

import br.fai.lds.medlink.domain.Vaccine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para criação de novas vacinas no prontuário médico.
 * 
 * <p>Contém as informações necessárias para registrar uma nova vacina
 * no histórico de imunização do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class VaccineCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String status;

    /**
     * Converte este DTO em uma entidade Vaccine.
     * 
     * @return Nova instância de Vaccine com os dados deste DTO
     */
    public Vaccine toEntity() {
        return Vaccine.builder()
                .name(this.name)
                .date(this.date)
                .build();
    }

    /**
     * Cria um DTO a partir de uma entidade Vaccine.
     * 
     * @param entity Entidade Vaccine a ser convertida
     * @return DTO com os dados da vacina ou null se entity for null
     */
    public static VaccineCreateDto fromEntity(Vaccine entity) {
        if (entity == null) return null;
        return VaccineCreateDto.builder()
                .name(entity.getName())
                .date(entity.getDate())
                .build();
    }
}

