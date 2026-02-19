package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Pesticide;

import java.util.List;

public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {

    List<Pesticide> findAllByFarmerId(Integer farmerId);

    List<Pesticide> findAllByFarmerIdAndPesticideTypeId(Integer farmerId, Integer pesticideTypeId);
}
