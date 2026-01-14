package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.Harvest;
import pl.farmapp.backend.service.HarvestService;

import java.util.List;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService service;

    public HarvestController(HarvestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Harvest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Harvest> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Harvest> create(@RequestBody Harvest harvest) {
        return service.create(harvest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Harvest> update(
            @PathVariable Integer id,
            @RequestBody Harvest harvest) {
        return service.update(id, harvest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Harvest> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/variety-season/{varietySeasonId}")
    public List<Harvest> getByVarietySeason(
            @PathVariable Integer varietySeasonId) {
        return service.getByVarietySeason(varietySeasonId);
    }
}
