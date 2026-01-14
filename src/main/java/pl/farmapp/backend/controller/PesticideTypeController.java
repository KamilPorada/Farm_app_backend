package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.PesticideType;
import pl.farmapp.backend.service.PesticideTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/pesticide-types")
public class PesticideTypeController {

    private final PesticideTypeService service;

    public PesticideTypeController(PesticideTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<PesticideType> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PesticideType> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PesticideType> create(@RequestBody PesticideType pesticideType) {
        return service.create(pesticideType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PesticideType> update(@PathVariable Integer id, @RequestBody PesticideType pesticideType) {
        return service.update(id, pesticideType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<PesticideType> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }
}
