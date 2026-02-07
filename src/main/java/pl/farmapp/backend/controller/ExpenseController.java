package pl.farmapp.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.ExpenseDto;
import pl.farmapp.backend.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExpenseDto> getAll(
            @RequestParam Integer farmerId
    ) {
        return service.getAll(farmerId);
    }

    @GetMapping("/category/{categoryId}")
    public List<ExpenseDto> getByCategory(
            @RequestParam Integer farmerId,
            @PathVariable Integer categoryId
    ) {
        return service.getByCategory(farmerId, categoryId);
    }

    @PostMapping
    public ExpenseDto create(
            @RequestParam Integer farmerId,
            @RequestBody ExpenseDto dto
    ) {
        return service.create(farmerId, dto);
    }

    @PutMapping("/{id}")
    public ExpenseDto update(
            @PathVariable Integer id,
            @RequestParam Integer farmerId,
            @RequestBody ExpenseDto dto
    ) {
        return service.update(id, farmerId, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Integer id,
            @RequestParam Integer farmerId
    ) {
        service.delete(id, farmerId);
    }
}
