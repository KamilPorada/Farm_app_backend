package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.ExpenseCategory;

import java.util.List;

public interface ExpenseCategoryRepository
        extends JpaRepository<ExpenseCategory, Integer> {

    List<ExpenseCategory> findByFarmerIdAndSeasonYearOrderByNameAsc(Integer farmerId, Integer seasonYear);
    List<ExpenseCategory> findByFarmerId(Integer farmerId);
    List<ExpenseCategory> findByFarmerIdAndSeasonYear(Integer farmerId, Integer seasonYear);


    boolean existsByFarmerIdAndSeasonYearAndNameIgnoreCase(Integer farmerId, Integer seasonYear, String name);}

