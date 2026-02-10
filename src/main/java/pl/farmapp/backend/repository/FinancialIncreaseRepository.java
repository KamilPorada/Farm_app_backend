package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FinancialIncrease;

import java.util.List;

public interface FinancialIncreaseRepository
        extends JpaRepository<FinancialIncrease, Integer> {

    List<FinancialIncrease> findByFarmerIdAndType_SeasonYear(
            Integer farmerId,
            Integer seasonYear
    );

    List<FinancialIncrease> findByFarmerIdAndType_IdAndType_SeasonYear(
            Integer farmerId,
            Integer typeId,
            Integer seasonYear
    );
}
