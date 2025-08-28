package br.fai.lds.medlink.port.service.report;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ReportTypeDto;

import java.util.List;

public interface ReportService {
    List<ReportTypeDto> getAvailableReportTypes();
    List<BirthdayReportDto> getBirthdayReport();
    List<PlanTypeReportDto> getPlanTypeReport();
    List<ConsultationReportDto> getConsultationReport();
    List<PatientReportDto> getPatientReport();
}
