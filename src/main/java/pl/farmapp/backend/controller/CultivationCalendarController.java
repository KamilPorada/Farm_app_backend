package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.CultivationCalendarDto;
import pl.farmapp.backend.service.CultivationCalendarService;

@RestController
@RequestMapping("/api/cultivation-calendar")
public class CultivationCalendarController {

    private final CultivationCalendarService service;

    public CultivationCalendarController(CultivationCalendarService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CultivationCalendarDto> create(
            @RequestBody CultivationCalendarDto dto) {

        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CultivationCalendarDto> update(
            @PathVariable Integer id,
            @RequestBody CultivationCalendarDto dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CultivationCalendarDto> getByFarmerAndYear(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear) {

        return service.getByFarmerAndYear(farmerId, seasonYear)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
