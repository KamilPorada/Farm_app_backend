package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.FinancialIncreaseType;
import pl.farmapp.backend.service.FinancialIncreaseTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/financial-increase-types")
public class FinancialIncreaseTypeController {

    private final FinancialIncreaseTypeService service;

    public FinancialIncreaseTypeController(FinancialIncreaseTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<FinancialIncreaseType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialIncreaseType> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FinancialIncreaseType> create(
            @RequestBody FinancialIncreaseType type) {
        return service.create(type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialIncreaseType> update(
            @PathVariable Integer id,
            @RequestBody FinancialIncreaseType type) {
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
    public List<FinancialIncreaseType> getByFarmer(
            @PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }
}
