package pl.farmapp.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.PointOfSaleDto;
import pl.farmapp.backend.entity.PointOfSale;
import pl.farmapp.backend.service.PointOfSaleService;

import java.util.List;

@RestController
@RequestMapping("/api/points-of-sale")
public class PointOfSaleController {

    private final PointOfSaleService service;

    public PointOfSaleController(PointOfSaleService service) {
        this.service = service;
    }

    @GetMapping("/farmer/{farmerId}")
    public List<PointOfSale> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PointOfSaleDto dto) {
        return service.create(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody PointOfSale pos
    ) {
        return service.update(id, pos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
