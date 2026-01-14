package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.FinancialDecrease;
import pl.farmapp.backend.service.FinancialDecreaseService;

import java.util.List;

@RestController
@RequestMapping("/api/financial-decreases")
public class FinancialDecreaseController {

    private final FinancialDecreaseService service;

    public FinancialDecreaseController(FinancialDecreaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinancialDecrease> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialDecrease> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialDecrease> create(
            @RequestBody FinancialDecrease decrease) {
        return service.create(decrease)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialDecrease> update(
            @PathVariable Integer id,
            @RequestBody FinancialDecrease decrease) {
        return service.update(id, decrease)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<FinancialDecrease> getByFarmer(
            @PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/type/{typeId}")
    public List<FinancialDecrease> getByType(
            @PathVariable Integer typeId) {
        return service.getByType(typeId);
    }
}
