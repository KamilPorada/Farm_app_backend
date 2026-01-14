package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.Pesticide;
import pl.farmapp.backend.service.PesticideService;

import java.util.List;

@RestController
@RequestMapping("/api/pesticides")
public class PesticideController {

    private final PesticideService service;

    public PesticideController(PesticideService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pesticide> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pesticide> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pesticide> create(@RequestBody Pesticide pesticide) {
        return service.create(pesticide)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pesticide> update(@PathVariable Integer id, @RequestBody Pesticide pesticide) {
        return service.update(id, pesticide)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Pesticide> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/type/{typeId}")
    public List<Pesticide> getByPesticideType(@PathVariable Integer typeId) {
        return service.getByPesticideType(typeId);
    }
}
