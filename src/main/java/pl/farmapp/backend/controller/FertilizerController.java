package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.service.FertilizerService;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizers")
public class FertilizerController {

    private final FertilizerService service;

    public FertilizerController(FertilizerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Fertilizer> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fertilizer> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fertilizer> create(@RequestBody Fertilizer fertilizer) {
        return service.create(fertilizer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fertilizer> update(@PathVariable Integer id, @RequestBody Fertilizer fertilizer) {
        return service.update(id, fertilizer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Fertilizer> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }
}
