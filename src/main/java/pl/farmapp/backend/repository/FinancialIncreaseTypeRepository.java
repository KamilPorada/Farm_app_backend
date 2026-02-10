package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FinancialIncreaseType;

import java.util.List;

public interface FinancialIncreaseTypeRepository
        extends JpaRepository<FinancialIncreaseType, Integer> {

    List<FinancialIncreaseType> findByFarmerIdAndSeasonYear(
            Integer farmerId,
            Integer seasonYear
    );

    boolean existsByFarmerIdAndNameAndSeasonYear(
            Integer farmerId,
            String name,
            Integer seasonYear
    );
}
