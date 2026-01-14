package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.VarietySeason;

import java.util.List;

public interface VarietySeasonRepository extends JpaRepository<VarietySeason, Integer> {

    List<VarietySeason> findByFarmerId(Integer farmerId);

    List<VarietySeason> findBySeasonYear(Integer seasonYear);
}
