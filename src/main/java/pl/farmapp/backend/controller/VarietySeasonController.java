package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.VarietySeasonDto;
import pl.farmapp.backend.service.VarietySeasonService;

import java.util.List;

@RestController
@RequestMapping("/api/varieties")
@RequiredArgsConstructor
public class VarietySeasonController {

    private final VarietySeasonService varietySeasonService;

    public VarietySeasonController(VarietySeasonService varietySeasonService) {
        this.varietySeasonService = varietySeasonService;
    }

    @PostMapping("/{farmerId}")
    public VarietySeasonDto create(
            @PathVariable Integer farmerId,
            @RequestBody VarietySeasonDto dto) {

        return varietySeasonService.create(farmerId, dto);
    }

    @PutMapping("/{id}")
    public VarietySeasonDto update(
            @PathVariable Integer id,
            @RequestBody VarietySeasonDto dto) {

        return varietySeasonService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        varietySeasonService.delete(id);
    }

    @GetMapping("/{farmerId}/{seasonYear}")
    public List<VarietySeasonDto> getByFarmerAndSeason(
            @PathVariable Integer farmerId,
            @PathVariable Integer seasonYear) {

        return varietySeasonService.getByFarmerAndSeason(farmerId, seasonYear);
    }
}
