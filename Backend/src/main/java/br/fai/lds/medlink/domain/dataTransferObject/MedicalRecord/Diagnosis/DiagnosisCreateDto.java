package br.fai.lds.medlink.domain.dataTransferObject.MedicalRecord.Diagnosis;

import br.fai.lds.medlink.domain.Diagnosis;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO para criação de novos diagnósticos no prontuário médico.
 * 
 * <p>Contém as informações necessárias para registrar um novo diagnóstico
 * no histórico médico do paciente.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class DiagnosisCreateDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Data do diagnóstico não pode ser futura")
    private LocalDate date;

    /**
     * Converte este DTO em uma entidade Diagnosis.
     * 
     * @return Nova instância de Diagnosis com os dados deste DTO
     */
    public Diagnosis toEntity() {
        return Diagnosis.builder()
                .description(this.description)
                .date(this.date)
                .build();
    }

    /**
     * Cria um DTO a partir de uma entidade Diagnosis.
     * 
     * @param entity Entidade Diagnosis a ser convertida
     * @return DTO com os dados do diagnóstico ou null se entity for null
     */
    public static DiagnosisCreateDto fromEntity(Diagnosis entity) {
        if (entity == null) return null;
        return DiagnosisCreateDto.builder()
                .description(entity.getDescription())
                .date(entity.getDate())
                .build();
    }
}
