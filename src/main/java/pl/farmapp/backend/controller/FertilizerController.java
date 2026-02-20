package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FertilizerDto;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.service.FertilizerService;

import java.util.List;

@RestController
@RequestMapping("/api/fertilizers")
@RequiredArgsConstructor
public class FertilizerController {

    private final FertilizerService service;

    public FertilizerController(FertilizerService service) {
        this.service = service;
    }

    @PostMapping("/{farmerId}")
    public Fertilizer create(
            @PathVariable Integer farmerId,
            @RequestBody FertilizerDto dto
    ) {
        return service.create(farmerId, dto);
    }

    @PutMapping("/{id}")
    public Fertilizer update(
            @PathVariable Integer id,
            @RequestBody FertilizerDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Fertilizer> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }
}