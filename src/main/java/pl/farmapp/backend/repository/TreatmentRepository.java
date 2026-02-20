package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Treatment;

import java.time.LocalDate;
import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {

    List<Treatment> findAllByFarmerIdAndTreatmentDateBetween(
            Integer farmerId,
            LocalDate start,
            LocalDate end
    );

}
