package pl.farmapp.backend.controller;



import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FinancialDecreaseTypeDto;
import pl.farmapp.backend.service.FinancialDecreaseTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/financial/decrease-types")
public class FinancialDecreaseTypeController {

    private final FinancialDecreaseTypeService service;

    public FinancialDecreaseTypeController(
            FinancialDecreaseTypeService service
    ) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public FinancialDecreaseTypeDto create(
            @RequestBody FinancialDecreaseTypeDto dto
    ) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public FinancialDecreaseTypeDto update(
            @PathVariable Integer id,
            @RequestBody FinancialDecreaseTypeDto dto
    ) {
        return service.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    // GET BY FARMER + SEASON
    @GetMapping
    public List<FinancialDecreaseTypeDto> getByFarmerAndSeason(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear
    ) {
        return service.getByFarmerAndSeason(farmerId, seasonYear);
    }
}
