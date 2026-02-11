package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.HarvestDto;
import pl.farmapp.backend.service.HarvestService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService harvestService;

    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @PostMapping("/{farmerId}")
    public HarvestDto create(
            @PathVariable Integer farmerId,
            @RequestBody HarvestDto dto
    ) {
        return harvestService.create(farmerId, dto);
    }

    @PutMapping("/{id}")
    public HarvestDto update(
            @PathVariable Integer id,
            @RequestBody HarvestDto dto
    ) {
        return harvestService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        harvestService.delete(id);
    }

    @GetMapping("/{farmerId}/{year}")
    public List<HarvestDto> getByFarmerAndYear(
            @PathVariable Integer farmerId,
            @PathVariable Integer year
    ) {
        return harvestService.getByFarmerAndYear(farmerId, year);
    }


}
