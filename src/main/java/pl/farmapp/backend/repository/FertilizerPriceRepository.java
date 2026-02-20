package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FertilizerPrice;

import java.util.Optional;

public interface FertilizerPriceRepository extends JpaRepository<FertilizerPrice, Integer> {

    Optional<FertilizerPrice> findByFarmerIdAndFertilizerIdAndSeasonYear(
            Integer farmerId,
            Integer fertilizerId,
            Integer seasonYear
    );

    Optional<FertilizerPrice>
    findTopByFarmerIdAndFertilizerIdOrderBySeasonYearDesc(
            Integer farmerId,
            Integer fertilizerId
    );
}