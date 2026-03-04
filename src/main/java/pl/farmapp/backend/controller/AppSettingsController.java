package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.AppSettingsDto;
import pl.farmapp.backend.service.AppSettingsService;

@RestController
@RequestMapping("/api/app-settings")
@RequiredArgsConstructor
public class AppSettingsController {

    private final AppSettingsService service;

    public AppSettingsController(AppSettingsService service) {
        this.service = service;
    }

    /* ===== GET ===== */
    @GetMapping("/{farmerId}")
    public ResponseEntity<AppSettingsDto> getSettings(
            @PathVariable Long farmerId
    ) {
        return service.getByFarmerId(Math.toIntExact(farmerId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /* ===== CREATE ===== */
    @PostMapping
    public ResponseEntity<AppSettingsDto> createSettings(
            @RequestBody AppSettingsDto dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    /* ===== UPDATE ===== */
    @PutMapping("/{farmerId}")
    public ResponseEntity<AppSettingsDto> updateSettings(
            @PathVariable Integer farmerId,
            @RequestBody AppSettingsDto dto
    ) {
        return ResponseEntity.ok(service.update(farmerId, dto));
    }
}
