package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.CultivationCalendar;

import java.util.Optional;

public interface CultivationCalendarRepository
        extends JpaRepository<CultivationCalendar, Integer> {

    Optional<CultivationCalendar> findByFarmerIdAndSeasonYear(
            Integer farmerId,
            Integer seasonYear
    );
}
