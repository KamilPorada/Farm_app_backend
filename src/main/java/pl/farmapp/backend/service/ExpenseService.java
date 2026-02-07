package pl.farmapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.ExpenseDto;
import pl.farmapp.backend.entity.Expense;
import pl.farmapp.backend.repository.ExpenseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<ExpenseDto> getAll(Integer farmerId) {
        return repository.findByFarmerIdOrderByExpenseDateDesc(farmerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getByCategory(Integer farmerId, Integer categoryId) {
        return repository
                .findByFarmerIdAndExpenseCategoryIdOrderByExpenseDateDesc(farmerId, categoryId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ExpenseDto create(Integer farmerId, ExpenseDto dto) {
        Expense expense = new Expense();
        expense.setFarmerId(farmerId);
        expense.setExpenseCategoryId(dto.getExpenseCategoryId());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setTitle(dto.getTitle());
        expense.setQuantity(dto.getQuantity());
        expense.setUnit(dto.getUnit());
        expense.setPrice(dto.getPrice());

        return toDto(repository.save(expense));
    }

    public ExpenseDto update(Integer id, Integer farmerId, ExpenseDto dto) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wydatek nie istnieje"));

        if (!expense.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        expense.setExpenseCategoryId(dto.getExpenseCategoryId());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setTitle(dto.getTitle());
        expense.setQuantity(dto.getQuantity());
        expense.setUnit(dto.getUnit());
        expense.setPrice(dto.getPrice());

        return toDto(repository.save(expense));
    }

    public void delete(Integer id, Integer farmerId) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wydatek nie istnieje"));

        if (!expense.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        repository.delete(expense);
    }

    private ExpenseDto toDto(Expense expense) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setExpenseCategoryId(expense.getExpenseCategoryId());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setTitle(expense.getTitle());
        dto.setQuantity(expense.getQuantity());
        dto.setUnit(expense.getUnit());
        dto.setPrice(expense.getPrice());
        return dto;
    }
}
