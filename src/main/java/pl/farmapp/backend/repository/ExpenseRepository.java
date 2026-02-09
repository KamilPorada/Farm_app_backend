package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    /* =======================
       FARMER + DATE RANGE
    ======================= */
    List<Expense> findAllByFarmerIdAndExpenseDateBetweenOrderByExpenseDateDesc(
            Integer farmerId,
            LocalDate from,
            LocalDate to
    );

    /* =======================
       FARMER + CATEGORY + DATE RANGE
    ======================= */
    List<Expense> findAllByFarmerIdAndExpenseCategoryIdAndExpenseDateBetweenOrderByExpenseDateDesc(
            Integer farmerId,
            Integer expenseCategoryId,
            LocalDate from,
            LocalDate to
    );

    /* =======================
       SAFETY
    ======================= */
    boolean existsByExpenseCategoryId(Integer expenseCategoryId);
}
