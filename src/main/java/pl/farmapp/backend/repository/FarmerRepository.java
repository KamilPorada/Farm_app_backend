package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Farmer;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Integer> {

    Optional<Farmer> findByExternalId(String externalId);
    boolean existsByExternalId(String externalId);

}
