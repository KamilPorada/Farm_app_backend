package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Fertilizer;

import java.util.List;
import java.util.Optional;

public interface FertilizerRepository extends JpaRepository<Fertilizer, Integer> {

    List<Fertilizer> findByFarmerId(Integer farmerId);
    Optional<Fertilizer> findByFarmerIdAndNameIgnoreCase(Integer farmerId, String name);

}