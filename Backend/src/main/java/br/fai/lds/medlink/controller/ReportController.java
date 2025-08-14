package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.port.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST responsável por fornecer os relatórios disponíveis ao médico
@RestController
@RequestMapping("/report")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Endpoint para gerar o relatório de aniversariantes
    @GetMapping("/birthday")
    public List<BirthdayReportDto> getBirthdayReport() {
        return reportService.getBirthdayReport();
    }

    // Endpoint para gerar o relatório por tipo de plano dos pacientes
    @GetMapping("/plan-type")
    public List<PlanTypeReportDto> getPlanTypeReport() {
        return reportService.getPlanTypeReport();
    }

    // Endpoint para gerar o relatório de consultas realizadas pelos pacientes
    @GetMapping("/consultations")
    public List<ConsultationReportDto> getConsultationReport() {
        return reportService.getConsultationReport();
    }

    // Endpoint para gerar o relatório geral dos pacientes
    @GetMapping("/patients")
    public List<PatientReportDto> getPatientReport() {
        return reportService.getPatientReport();
    }
}
