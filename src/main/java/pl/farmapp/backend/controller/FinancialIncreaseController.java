package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FinancialIncreaseDto;
import pl.farmapp.backend.service.FinancialIncreaseService;

import java.util.List;

@RestController
@RequestMapping("/api/financial/increases")
public class FinancialIncreaseController {

    private final FinancialIncreaseService service;

    public FinancialIncreaseController(
            FinancialIncreaseService service
    ) {
        this.service = service;
    }

    @PostMapping
    public FinancialIncreaseDto create(
            @RequestBody FinancialIncreaseDto dto
    ) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public FinancialIncreaseDto update(
            @PathVariable Integer id,
            @RequestBody FinancialIncreaseDto dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping
    public List<FinancialIncreaseDto> getByFarmerAndSeason(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear
    ) {
        return service.getByFarmerAndSeason(farmerId, seasonYear);
    }

    @GetMapping("/by-type")
    public List<FinancialIncreaseDto> getByFarmerSeasonAndType(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear,
            @RequestParam Integer typeId
    ) {
        return service.getByFarmerSeasonAndType(
                farmerId,
                seasonYear,
                typeId
        );
    }
}
