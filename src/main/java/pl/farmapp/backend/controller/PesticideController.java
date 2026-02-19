package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.PesticideDto;
import pl.farmapp.backend.service.PesticideService;

import java.util.List;

@RestController
@RequestMapping("/api/pesticides")
@RequiredArgsConstructor
public class PesticideController {

    private final PesticideService service;

    public PesticideController(PesticideService service) {
        this.service = service;
    }

    // 🔹 wszystkie środki
    @GetMapping
    public List<PesticideDto> getAll(@RequestParam Integer farmerId) {
        return service.getAll(farmerId);
    }

    // 🔹 filtr po typie
    @GetMapping("/by-type/{typeId}")
    public List<PesticideDto> getByType(
            @RequestParam Integer farmerId,
            @PathVariable Integer typeId) {
        return service.getByType(farmerId, typeId);
    }

    // 🔹 dodaj
    @PostMapping
    public PesticideDto create(
            @RequestParam Integer farmerId,
            @RequestBody PesticideDto dto) {
        return service.create(farmerId, dto);
    }

    // 🔹 edytuj
    @PutMapping("/{id}")
    public PesticideDto update(
            @PathVariable Integer id,
            @RequestParam Integer farmerId,
            @RequestBody PesticideDto dto) {
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
