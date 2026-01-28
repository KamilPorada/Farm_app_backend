package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.AppSettings;

import java.util.Optional;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {

    Optional<AppSettings> findByFarmerId(Long farmerId);

    boolean existsByFarmerId(Long farmerId);
}
