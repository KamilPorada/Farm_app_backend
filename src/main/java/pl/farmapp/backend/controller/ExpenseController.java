package pl.farmapp.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.ExpenseDto;
import pl.farmapp.backend.service.ExpenseCategoryService;
import pl.farmapp.backend.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    /* =======================
       GET ALL (BY YEAR)
    ======================= */
    @GetMapping
    public List<ExpenseDto> getAll(
            @RequestParam Integer farmerId,
            @RequestParam Integer year
    ) {
        return service.getAll(farmerId, year);
    }

    /* =======================
       GET BY CATEGORY (BY YEAR)
    ======================= */
    @GetMapping("/category/{categoryId}")
    public List<ExpenseDto> getByCategory(
            @RequestParam Integer farmerId,
            @RequestParam Integer year,
            @PathVariable Integer categoryId
    ) {
        return service.getByCategory(farmerId, categoryId, year);
    }

    /* =======================
       CREATE
    ======================= */
    @PostMapping
    public ExpenseDto create(
            @RequestParam Integer farmerId,
            @RequestBody ExpenseDto dto
    ) {
        return service.create(farmerId, dto);
    }

    /* =======================
       UPDATE
    ======================= */
    @PutMapping("/{id}")
    public ExpenseDto update(
            @PathVariable Integer id,
            @RequestParam Integer farmerId,
            @RequestBody ExpenseDto dto
    ) {
        return service.update(id, farmerId, dto);
    }

    /* =======================
       DELETE
    ======================= */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Integer id,
            @RequestParam Integer farmerId
    ) {
        service.delete(id, farmerId);
    }
}
