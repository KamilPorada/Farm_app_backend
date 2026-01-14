package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.FinancialDecreaseType;
import pl.farmapp.backend.service.FinancialDecreaseTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/financial-decrease-types")
public class FinancialDecreaseTypeController {

    private final FinancialDecreaseTypeService service;

    public FinancialDecreaseTypeController(FinancialDecreaseTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinancialDecreaseType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialDecreaseType> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialDecreaseType> create(
            @RequestBody FinancialDecreaseType type) {
        return service.create(type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialDecreaseType> update(
            @PathVariable Integer id,
            @RequestBody FinancialDecreaseType type) {
        return service.update(id, type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<FinancialDecreaseType> getByFarmer(
            @PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }
}
