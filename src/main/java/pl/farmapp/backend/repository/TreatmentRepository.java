package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Treatment;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {

    List<Treatment> findByFarmerId(Integer farmerId);

    List<Treatment> findByPesticideId(Integer pesticideId);
}
