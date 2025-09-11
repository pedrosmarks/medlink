package br.fai.lds.medlink.domain.dataTransferObject.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {
    private Integer medicoId;
    private String status;
}