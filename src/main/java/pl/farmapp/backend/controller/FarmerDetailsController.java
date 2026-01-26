package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FarmerDetailsDto;
import pl.farmapp.backend.service.FarmerDetailsService;

@RestController
@RequestMapping("/api/farmer-details")
public class FarmerDetailsController {

    private final FarmerDetailsService service;

    public FarmerDetailsController(FarmerDetailsService service) {
        this.service = service;
    }
    @GetMapping("/{farmerId}")
    public ResponseEntity<FarmerDetailsDto> get(@PathVariable Long farmerId) {
        return service.getByFarmerId(farmerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public FarmerDetailsDto create(@RequestBody FarmerDetailsDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{farmerId}")
    public FarmerDetailsDto update(
            @PathVariable Long farmerId,
            @RequestBody FarmerDetailsDto dto
    ) {
        return service.update(farmerId, dto);
    }

    @DeleteMapping("/{farmerId}")
    public void delete(@PathVariable Long farmerId) {
        service.delete(farmerId);
    }
}
