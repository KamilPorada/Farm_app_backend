package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.Fertigation;
import pl.farmapp.backend.service.FertigationService;

import java.util.List;

@RestController
@RequestMapping("/api/fertigations")
public class FertigationController {

    private final FertigationService service;

    public FertigationController(FertigationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Fertigation> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fertigation> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fertigation> create(@RequestBody Fertigation fertigation) {
        return service.create(fertigation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fertigation> update(@PathVariable Integer id, @RequestBody Fertigation fertigation) {
        return service.update(id, fertigation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Fertigation> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/fertilizer/{fertilizerId}")
    public List<Fertigation> getByFertilizer(@PathVariable Integer fertilizerId) {
        return service.getByFertilizer(fertilizerId);
    }
}
