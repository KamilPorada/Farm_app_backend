package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.Treatment;
import pl.farmapp.backend.service.TreatmentService;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Treatment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Treatment> create(@RequestBody Treatment treatment) {
        return service.create(treatment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treatment> update(@PathVariable Integer id, @RequestBody Treatment treatment) {
        return service.update(id, treatment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Treatment> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/pesticide/{pesticideId}")
    public List<Treatment> getByPesticide(@PathVariable Integer pesticideId) {
        return service.getByPesticide(pesticideId);
    }
}
