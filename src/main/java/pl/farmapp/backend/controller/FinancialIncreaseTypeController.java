package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FinancialIncreaseTypeDto;
import pl.farmapp.backend.service.FinancialIncreaseTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/financial/increase-types")
public class FinancialIncreaseTypeController {

    private final FinancialIncreaseTypeService service;

    public FinancialIncreaseTypeController(
            FinancialIncreaseTypeService service
    ) {
        this.service = service;
    }

    @PostMapping
    public FinancialIncreaseTypeDto create(
            @RequestBody FinancialIncreaseTypeDto dto
    ) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public FinancialIncreaseTypeDto update(
            @PathVariable Integer id,
            @RequestBody FinancialIncreaseTypeDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping
    public List<FinancialIncreaseTypeDto> getByFarmerAndSeason(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear
    ) {
        return service.getByFarmerAndSeason(farmerId, seasonYear);
    }
}
