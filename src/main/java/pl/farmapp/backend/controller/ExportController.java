package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.service.ExportService;
import pl.farmapp.backend.service.MailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/{farmerId}")
    public ResponseEntity<String> export(@PathVariable Integer farmerId) throws Exception {

        String json = exportService.generateJson(farmerId);

        return ResponseEntity.ok(json);
    }

    @PostMapping("/email/{farmerId}")
    public ResponseEntity<?> sendExportEmail(@PathVariable Integer farmerId) throws Exception {

        exportService.sendExportToEmail(farmerId);

        return ResponseEntity.ok("Backup wysłany na email");
    }
}