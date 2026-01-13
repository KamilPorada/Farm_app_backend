package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.FarmerTunnels;
import pl.farmapp.backend.service.FarmerTunnelsService;

import java.util.List;

@RestController
@RequestMapping("/api/farmer-tunnels")
public class FarmerTunnelsController {

    private final FarmerTunnelsService farmerTunnelsService;

    public FarmerTunnelsController(FarmerTunnelsService farmerTunnelsService) {
        this.farmerTunnelsService = farmerTunnelsService;
    }

    @GetMapping
    public List<FarmerTunnels> getAll() {
        return farmerTunnelsService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerTunnels> getById(@PathVariable Integer id) {
        return farmerTunnelsService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FarmerTunnels> create(@RequestBody FarmerTunnels farmerTunnels) {
        return farmerTunnelsService.create(farmerTunnels)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmerTunnels> update(@PathVariable Integer id, @RequestBody FarmerTunnels farmerTunnels) {
        return farmerTunnelsService.update(id, farmerTunnels)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        farmerTunnelsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
