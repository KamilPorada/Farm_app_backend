package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Fertigation;

import java.util.List;

@Repository
public interface FertigationRepository extends JpaRepository<Fertigation, Integer> {

    List<Fertigation> findByFarmerId(Integer farmerId);

    List<Fertigation> findByFertilizerId(Integer fertilizerId);
}
