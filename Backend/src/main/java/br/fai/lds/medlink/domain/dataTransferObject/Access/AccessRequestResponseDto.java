package br.fai.lds.medlink.domain.dataTransferObject.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestResponseDto {
    private int medicoId;
    private String medicoName;
    private String medicoSpecialty;
    private String status;
}