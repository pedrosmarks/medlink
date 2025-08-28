package br.fai.lds.medlink.controller;

import br.fai.lds.medlink.domain.ApiResponse;
import br.fai.lds.medlink.domain.dataTransferObject.Report.BirthdayReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ConsultationReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PatientReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.PlanTypeReportDto;
import br.fai.lds.medlink.domain.dataTransferObject.Report.ReportTypeDto;
import br.fai.lds.medlink.port.service.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin
@Slf4j
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public ResponseEntity<ApiResponse<List<ReportTypeDto>>> getAvailableReports() {
        try {
            List<ReportTypeDto> reports = reportService.getAvailableReportTypes();
            return ResponseEntity.ok(new ApiResponse<>("Tipos de relatórios recuperados com sucesso.", reports));
        } catch (Exception e) {
            log.error("Erro ao buscar tipos de relatórios: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro interno do servidor."));
        }
    }

    @GetMapping("/birthday")
    public ResponseEntity<ApiResponse<List<BirthdayReportDto>>> getBirthdayReport() {
        try {
            List<BirthdayReportDto> report = reportService.getBirthdayReport();
            return ResponseEntity.ok(new ApiResponse<>("Relatório de aniversariantes gerado com sucesso.", report));
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de aniversariantes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro ao gerar relatório de aniversariantes."));
        }
    }

    @GetMapping("/plan-type")
    public ResponseEntity<ApiResponse<List<PlanTypeReportDto>>> getPlanTypeReport() {
        try {
            List<PlanTypeReportDto> report = reportService.getPlanTypeReport();
            return ResponseEntity.ok(new ApiResponse<>("Relatório por tipo de plano gerado com sucesso.", report));
        } catch (Exception e) {
            log.error("Erro ao gerar relatório por tipo de plano: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro ao gerar relatório por tipo de plano."));
        }
    }

    @GetMapping("/consultations")
    public ResponseEntity<ApiResponse<List<ConsultationReportDto>>> getConsultationReport() {
        try {
            List<ConsultationReportDto> report = reportService.getConsultationReport();
            return ResponseEntity.ok(new ApiResponse<>("Relatório de consultas gerado com sucesso.", report));
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de consultas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro ao gerar relatório de consultas."));
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<PatientReportDto>>> getPatientReport() {
        try {
            List<PatientReportDto> report = reportService.getPatientReport();
            return ResponseEntity.ok(new ApiResponse<>("Relatório de pacientes gerado com sucesso.", report));
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de pacientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erro ao gerar relatório de pacientes."));
        }
    }
}
