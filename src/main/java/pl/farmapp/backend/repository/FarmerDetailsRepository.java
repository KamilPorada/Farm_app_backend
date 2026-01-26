package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.FarmerDetails;

import java.util.Optional;

public interface FarmerDetailsRepository extends JpaRepository<FarmerDetails, Long> {

    Optional<FarmerDetails> findByFarmerId(Long farmerId);

    boolean existsByFarmerId(Long farmerId);
}
