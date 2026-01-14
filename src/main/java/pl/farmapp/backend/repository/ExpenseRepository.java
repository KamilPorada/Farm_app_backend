package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Expense;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByFarmerId(Integer farmerId);

    List<Expense> findByExpenseCategoryId(Integer categoryId);

    List<Expense> findByFarmerIdAndExpenseDateBetween(
            Integer farmerId,
            LocalDate from,
            LocalDate to
    );
}
