package pl.farmapp.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.ExpenseCategoryDto;
import pl.farmapp.backend.service.ExpenseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService service;

    public ExpenseCategoryController(ExpenseCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExpenseCategoryDto> getAll(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear
    ) {
        return service.getAll(farmerId, seasonYear);
    }

    @PostMapping
    public ExpenseCategoryDto create(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear,
            @RequestBody ExpenseCategoryDto dto
    ) {
        return service.create(farmerId, seasonYear, dto);
    }

    @PutMapping("/{id}")
    public ExpenseCategoryDto update(
            @PathVariable Integer id,
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear,
            @RequestBody ExpenseCategoryDto dto
    ) {
        return service.update(id, farmerId, seasonYear, dto);
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