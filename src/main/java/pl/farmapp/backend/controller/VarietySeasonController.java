package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.VarietySeason;
import pl.farmapp.backend.service.VarietySeasonService;

import java.util.List;

@RestController
@RequestMapping("/api/variety-seasons")
public class VarietySeasonController {

    private final VarietySeasonService service;

    public VarietySeasonController(VarietySeasonService service) {
        this.service = service;
    }

    @GetMapping
    public List<VarietySeason> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VarietySeason> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VarietySeason> create(
            @RequestBody VarietySeason varietySeason) {
        return service.create(varietySeason)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VarietySeason> update(
            @PathVariable Integer id,
            @RequestBody VarietySeason varietySeason) {
        return service.update(id, varietySeason)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<VarietySeason> getByFarmer(
            @PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/season/{year}")
    public List<VarietySeason> getBySeason(
            @PathVariable Integer year) {
        return service.getBySeasonYear(year);
    }
}
