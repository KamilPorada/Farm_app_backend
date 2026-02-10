package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FinancialDecrease;

import java.util.List;

public interface FinancialDecreaseRepository
        extends JpaRepository<FinancialDecrease, Integer> {

    List<FinancialDecrease> findByFarmerIdAndType_SeasonYear(
            Integer farmerId,
            Integer seasonYear
    );

    List<FinancialDecrease> findByFarmerIdAndType_IdAndType_SeasonYear(
            Integer farmerId,
            Integer typeId,
            Integer seasonYear
    );
}
