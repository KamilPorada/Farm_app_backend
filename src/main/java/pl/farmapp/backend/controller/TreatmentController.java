package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.TreatmentDto;
import pl.farmapp.backend.service.TreatmentService;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @PostMapping
    public TreatmentDto create(@RequestBody TreatmentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TreatmentDto update(@PathVariable Integer id,
                               @RequestBody TreatmentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/farmer/{farmerId}/{year}")
    public List<TreatmentDto> getByFarmerAndYear(
            @PathVariable Integer farmerId,
            @PathVariable Integer year) {
        return service.getByFarmerAndYear(farmerId, year);
    }
}
