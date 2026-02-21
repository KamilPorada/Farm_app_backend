package pl.farmapp.backend.repository;

import pl.farmapp.backend.entity.Fertigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FertigationRepository extends JpaRepository<Fertigation, Integer> {

    List<Fertigation> findByFarmerId(Integer farmerId);

    List<Fertigation> findByFarmerIdAndFertigationDateBetween(
            Integer farmerId,
            LocalDate startDate,
            LocalDate endDate
    );
}