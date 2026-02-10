package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FinancialDecreaseType;

import java.util.List;

public interface FinancialDecreaseTypeRepository
        extends JpaRepository<FinancialDecreaseType, Integer> {

    List<FinancialDecreaseType> findByFarmerIdAndSeasonYear(
            Integer farmerId,
            Integer seasonYear
    );

    boolean existsByFarmerIdAndNameAndSeasonYear(
            Integer farmerId,
            String name,
            Integer seasonYear
    );
}
