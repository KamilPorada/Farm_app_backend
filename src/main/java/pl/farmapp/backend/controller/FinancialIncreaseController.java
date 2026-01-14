package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.FinancialIncrease;
import pl.farmapp.backend.service.FinancialIncreaseService;

import java.util.List;

@RestController
@RequestMapping("/api/financial-increases")
public class FinancialIncreaseController {

    private final FinancialIncreaseService service;

    public FinancialIncreaseController(FinancialIncreaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinancialIncrease> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialIncrease> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialIncrease> create(
            @RequestBody FinancialIncrease increase) {
        return service.create(increase)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialIncrease> update(
            @PathVariable Integer id,
            @RequestBody FinancialIncrease increase) {
        return service.update(id, increase)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<FinancialIncrease> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/type/{typeId}")
    public List<FinancialIncrease> getByType(@PathVariable Integer typeId) {
        return service.getByType(typeId);
    }
}
