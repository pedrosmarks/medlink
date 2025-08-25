package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.port.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controlador REST responsável por fornecer os relatórios disponíveis ao médico
@RestController
@RequestMapping("/report")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Endpoint para compatibilidade com frontend - lista relatórios disponíveis
    @GetMapping("/relatorios")
    public List<Map<String, String>> getAvailableReports() {
        return List.of(
            Map.of(
                "id", "1",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de atendimentos",
                "descricao", "Resumo dos atendimentos realizados no mês."
            ),
            Map.of(
                "id", "2",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório financeiro",
                "descricao", "Resumo financeiro mensal."
            ),
            Map.of(
                "id", "3",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório de aniversariantes",
                "descricao", "Lista de pacientes aniversariantes do mês."
            ),
            Map.of(
                "id", "4",
                "icone", "https://cdn-icons-png.flaticon.com/512/3135/3135715.png",
                "titulo", "Relatório por tipo de plano",
                "descricao", "Distribuição de pacientes por tipo de plano."
            )
        );
    }

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
