package br.fai.lds.medlink.domain.dataTransferObject.Report;

import lombok.Builder;
import lombok.Data;

// DTO utilizado para representar o plano de saúde associado a cada paciente em um relatório
@Data
@Builder
public class PlanTypeReportDto {
    private String patientName;
    private String planType;
}
