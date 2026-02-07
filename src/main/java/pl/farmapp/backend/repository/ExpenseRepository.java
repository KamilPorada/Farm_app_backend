package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByFarmerIdOrderByExpenseDateDesc(Integer farmerId);

    List<Expense> findByFarmerIdAndExpenseCategoryIdOrderByExpenseDateDesc(
            Integer farmerId,
            Integer expenseCategoryId
    );

    List<Expense> findByFarmerIdAndExpenseDateBetween(
            Integer farmerId,
            LocalDate from,
            LocalDate to
    );

    boolean existsByExpenseCategoryId(Integer expenseCategoryId);
}
