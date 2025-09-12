package br.fai.lds.medlink.domain.dataTransferObject.Access;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {
    
    @NotNull(message = "ID do médico é obrigatório")
    private int medicoId;
    
    private String status;
    
    // Método para normalizar o status
    public String getStatus() {
        return status != null ? status.toUpperCase() : null;
    }
    
    public void setStatus(String status) {
        this.status = status != null ? status.toUpperCase() : null;
    }
}