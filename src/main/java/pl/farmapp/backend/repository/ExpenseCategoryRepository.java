package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.ExpenseCategory;

import java.util.List;

public interface ExpenseCategoryRepository
        extends JpaRepository<ExpenseCategory, Integer> {

    List<ExpenseCategory> findByFarmerIdOrderByNameAsc(Integer farmerId);

    boolean existsByFarmerIdAndNameIgnoreCase(Integer farmerId, String name);
}
