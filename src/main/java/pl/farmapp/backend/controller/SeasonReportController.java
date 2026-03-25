package pl.farmapp.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.service.report.SeasonReportService;

@RestController
@RequestMapping("/api/reports")
public class SeasonReportController {

    private final SeasonReportService reportService;

    public SeasonReportController(SeasonReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/season/{year}")
    public ResponseEntity<byte[]> generateSeasonReport(
            @PathVariable Integer year,
            @RequestParam Integer farmerId
    ) {

        byte[] report = reportService.generateSeasonReport(farmerId, year);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=raport_sezon_" + year + ".docx")
                .header(HttpHeaders.CONTENT_TYPE,
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .body(report);
    }
}