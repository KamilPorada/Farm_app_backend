package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.PesticideType;

import java.util.List;

public interface PesticideTypeRepository extends JpaRepository<PesticideType, Integer> {

    List<PesticideType> findByFarmerIdOrderByNameAsc(Integer farmerId);

    boolean existsByFarmerIdAndNameIgnoreCase(Integer farmerId, String name);
}
