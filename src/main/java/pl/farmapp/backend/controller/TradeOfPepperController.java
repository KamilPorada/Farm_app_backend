package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.TradeOfPepper;
import pl.farmapp.backend.service.TradeOfPepperService;

import java.util.List;

@RestController
@RequestMapping("/api/trades-of-pepper")
public class TradeOfPepperController {

    private final TradeOfPepperService tradeService;

    public TradeOfPepperController(TradeOfPepperService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping
    public List<TradeOfPepper> getAll() {
        return tradeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeOfPepper> getById(@PathVariable Integer id) {
        return tradeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TradeOfPepper> create(@RequestBody TradeOfPepper trade) {
        return tradeService.create(trade)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeOfPepper> update(
            @PathVariable Integer id,
            @RequestBody TradeOfPepper trade) {
        return tradeService.update(id, trade)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tradeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<TradeOfPepper> getByFarmer(@PathVariable Integer farmerId) {
        return tradeService.getByFarmer(farmerId);
    }

    @GetMapping("/point-of-sale/{posId}")
    public List<TradeOfPepper> getByPointOfSale(@PathVariable Integer posId) {
        return tradeService.getByPointOfSale(posId);
    }
}
