package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.TradeOfPepperCreateDto;
import pl.farmapp.backend.dto.TradeOfPepperDto;
import pl.farmapp.backend.mapper.TradeOfPepperMapper;
import pl.farmapp.backend.service.TradeOfPepperService;

import java.util.List;

@RestController
@RequestMapping("/api/trades-of-pepper")
public class TradeOfPepperController {

    private final TradeOfPepperService service;

    public TradeOfPepperController(TradeOfPepperService service) {
        this.service = service;
    }

    @GetMapping("/farmer/{farmerId}")
    public List<TradeOfPepperDto> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId)
                .stream()
                .map(TradeOfPepperMapper::toDto)
                .toList();
    }

    @GetMapping("/farmer/{farmerId}/year/{year}")
    public List<TradeOfPepperDto> getByFarmerAndYear(
            @PathVariable Integer farmerId,
            @PathVariable int year) {

        return service.getByFarmerAndYear(farmerId, year)
                .stream()
                .map(TradeOfPepperMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TradeOfPepperDto> create(
            @RequestBody TradeOfPepperCreateDto dto) {

        return service.create(dto)
                .map(TradeOfPepperMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeOfPepperDto> update(
            @PathVariable Integer id,
            @RequestBody TradeOfPepperCreateDto dto) {

        return service.update(id, dto)
                .map(TradeOfPepperMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
