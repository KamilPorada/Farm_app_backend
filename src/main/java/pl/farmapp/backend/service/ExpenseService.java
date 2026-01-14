package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Expense;
import pl.farmapp.backend.repository.ExpenseCategoryRepository;
import pl.farmapp.backend.repository.ExpenseRepository;
import pl.farmapp.backend.repository.FarmerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final FarmerRepository farmerRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          FarmerRepository farmerRepository,
                          ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.farmerRepository = farmerRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getById(Integer id) {
        return expenseRepository.findById(id);
    }

    public Optional<Expense> create(Expense expense) {

        if (expense.getFarmer() == null ||
                expense.getFarmer().getId() == null ||
                !farmerRepository.existsById(expense.getFarmer().getId())) {
            return Optional.empty();
        }

        if (expense.getExpenseCategory() == null ||
                expense.getExpenseCategory().getId() == null ||
                !expenseCategoryRepository.existsById(expense.getExpenseCategory().getId())) {
            return Optional.empty();
        }

        return Optional.of(expenseRepository.save(expense));
    }

    public Optional<Expense> update(Integer id, Expense updated) {
        return expenseRepository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getExpenseCategory() != null && updated.getExpenseCategory().getId() != null) {
                if (!expenseCategoryRepository.existsById(updated.getExpenseCategory().getId())) {
                    return Optional.empty();
                }
                existing.setExpenseCategory(updated.getExpenseCategory());
            }

            existing.setExpenseDate(updated.getExpenseDate());
            existing.setTitle(updated.getTitle());
            existing.setQuantity(updated.getQuantity());
            existing.setUnit(updated.getUnit());
            existing.setPrice(updated.getPrice());

            return Optional.of(expenseRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getByFarmer(Integer farmerId) {
        return expenseRepository.findByFarmerId(farmerId);
    }

    public List<Expense> getByCategory(Integer categoryId) {
        return expenseRepository.findByExpenseCategoryId(categoryId);
    }
}
