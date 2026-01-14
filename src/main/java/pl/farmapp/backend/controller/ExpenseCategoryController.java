package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.ExpenseCategory;
import pl.farmapp.backend.service.ExpenseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @GetMapping
    public List<ExpenseCategory> getAll() {
        return expenseCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseCategory> getById(@PathVariable Integer id) {
        return expenseCategoryService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpenseCategory> create(@RequestBody ExpenseCategory category) {
        return expenseCategoryService.create(category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseCategory> update(
            @PathVariable Integer id,
            @RequestBody ExpenseCategory category) {
        return expenseCategoryService.update(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        expenseCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<ExpenseCategory> getByFarmer(@PathVariable Integer farmerId) {
        return expenseCategoryService.getByFarmer(farmerId);
    }
}
