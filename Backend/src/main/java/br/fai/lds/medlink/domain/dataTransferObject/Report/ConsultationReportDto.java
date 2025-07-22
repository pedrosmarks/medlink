package br.fai.lds.medlink.domain.dataTransferObject.Report;

import lombok.Builder;
import lombok.Data;

// DTO utilizado para representar os dados de consultas médicas em relatórios:
@Data
@Builder
public class ConsultationReportDto {
    private String patientName;
    private String date;
    private String reason;
}
