package br.fai.lds.medlink.domain.dataTransferObject.Access;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {
    
    @NotNull(message = "ID do médico é obrigatório")
    private int medicoId;
    
    @Pattern(regexp = "^(PENDENTE|ACEITA|RECUSADA)$", message = "Status deve ser PENDENTE, ACEITA ou RECUSADA")
    private String status;
}