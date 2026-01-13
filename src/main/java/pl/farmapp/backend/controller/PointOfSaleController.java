package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.PointOfSale;
import pl.farmapp.backend.service.PointOfSaleService;

import java.util.List;

@RestController
@RequestMapping("/api/point-of-sale")
public class PointOfSaleController {

    private final PointOfSaleService pointOfSaleService;

    public PointOfSaleController(PointOfSaleService pointOfSaleService) {
        this.pointOfSaleService = pointOfSaleService;
    }

    @GetMapping
    public List<PointOfSale> getAll() {
        return pointOfSaleService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointOfSale> getById(@PathVariable Integer id) {
        return pointOfSaleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PointOfSale> create(@RequestBody PointOfSale pos) {
        return pointOfSaleService.create(pos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointOfSale> update(
            @PathVariable Integer id,
            @RequestBody PointOfSale pos) {
        return pointOfSaleService.update(id, pos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pointOfSaleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint po farmerze
    @GetMapping("/farmer/{farmerId}")
    public List<PointOfSale> getByFarmer(@PathVariable Integer farmerId) {
        return pointOfSaleService.getByFarmer(farmerId);
    }
}
