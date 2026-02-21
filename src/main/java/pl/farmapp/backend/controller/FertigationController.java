package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FertigationDto;
import pl.farmapp.backend.service.FertigationService;

import java.util.List;

@RestController
@RequestMapping("/api/fertigations")
public class FertigationController {

    private final FertigationService fertigationService;

    public FertigationController(FertigationService fertigationService) {
        this.fertigationService = fertigationService;
    }

    @PostMapping
    public FertigationDto create(
            @RequestHeader("farmerId") Integer farmerId,
            @RequestBody FertigationDto dto
    ) {
        return fertigationService.create(farmerId, dto);
    }

    @PutMapping("/{id}")
    public FertigationDto update(
            @PathVariable Integer id,
            @RequestHeader("farmerId") Integer farmerId,
            @RequestBody FertigationDto dto
    ) {
        return fertigationService.update(id, farmerId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Integer id,
            @RequestHeader("farmerId") Integer farmerId
    ) {
        fertigationService.delete(id, farmerId);
    }

    @GetMapping
    public List<FertigationDto> getByFarmer(
            @RequestHeader("farmerId") Integer farmerId
    ) {
        return fertigationService.getByFarmer(farmerId);
    }

    @GetMapping("/season/{year}")
    public List<FertigationDto> getBySeason(
            @PathVariable Integer year,
            @RequestHeader("farmerId") Integer farmerId
    ) {
        return fertigationService.getBySeason(farmerId, year);
    }
}