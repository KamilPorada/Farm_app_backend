package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FinancialDecreaseDto;
import pl.farmapp.backend.service.FinancialDecreaseService;

import java.util.List;

@RestController
@RequestMapping("/api/financial/decreases")
public class FinancialDecreaseController {

    private final FinancialDecreaseService service;

    public FinancialDecreaseController(
            FinancialDecreaseService service
    ) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public FinancialDecreaseDto create(
            @RequestBody FinancialDecreaseDto dto
    ) {
        return service.create(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public FinancialDecreaseDto update(
            @PathVariable Integer id,
            @RequestBody FinancialDecreaseDto dto
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
    public List<FinancialDecreaseDto> getByFarmerAndSeason(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear
    ) {
        return service.getByFarmerAndSeason(farmerId, seasonYear);
    }

    @GetMapping("/by-type")
    public List<FinancialDecreaseDto> getByFarmerSeasonAndType(
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
