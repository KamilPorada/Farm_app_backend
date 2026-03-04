package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.AppSettings;

import java.util.Optional;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Integer> {

    Optional<AppSettings> findByFarmerId(Integer farmerId);

    boolean existsByFarmerId(Integer farmerId);
}
