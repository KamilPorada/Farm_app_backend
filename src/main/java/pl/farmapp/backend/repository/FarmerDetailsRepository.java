package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FarmerDetails;

import java.util.Optional;

public interface FarmerDetailsRepository extends JpaRepository<FarmerDetails, Integer> {

    Optional<FarmerDetails> findByFarmerId(Integer farmerId);

    boolean existsByFarmerId(Integer farmerId);
}