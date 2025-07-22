package br.fai.lds.medlink.domain.dataTransferObject.Report;

import lombok.Builder;
import lombok.Data;

// DTO utilizado para representar os dados de um paciente em relatórios:

@Data
@Builder
public class PatientReportDto {
    private String name;
    private String email;
    private String plan;
    private boolean active;
}
