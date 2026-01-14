package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.PesticideType;

import java.util.List;

@Repository
public interface PesticideTypeRepository extends JpaRepository<PesticideType, Integer> {

    List<PesticideType> findByFarmerId(Integer farmerId);
}
