package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Harvest;

import java.util.List;

public interface HarvestRepository extends JpaRepository<Harvest, Integer> {

    List<Harvest> findByFarmerId(Integer farmerId);

    List<Harvest> findByVarietySeasonId(Integer varietySeasonId);
}
