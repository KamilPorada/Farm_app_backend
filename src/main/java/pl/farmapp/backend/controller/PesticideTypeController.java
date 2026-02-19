package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.PesticideTypeDto;
import pl.farmapp.backend.service.PesticideTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/pesticide-types")
@RequiredArgsConstructor
public class PesticideTypeController {

    private final PesticideTypeService service;

    public PesticideTypeController(PesticideTypeService service) {
        this.service = service;
    }

    // 🔹 pobierz wszystkie dla farmera
    @GetMapping
    public List<PesticideTypeDto> getAll(@RequestParam Integer farmerId) {
        return service.getAll(farmerId);
    }

    // 🔹 utwórz
    @PostMapping
    public PesticideTypeDto create(
            @RequestParam Integer farmerId,
            @RequestBody PesticideTypeDto dto) {
        return service.create(farmerId, dto);
    }

    // 🔹 edytuj
    @PutMapping("/{id}")
    public PesticideTypeDto update(
            @PathVariable Integer id,
            @RequestParam Integer farmerId,
            @RequestBody PesticideTypeDto dto) {
        return service.update(id, farmerId, dto);
    }

    // 🔹 usuń
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Integer id,
            @RequestParam Integer farmerId) {
        service.delete(id, farmerId);
    }
}
