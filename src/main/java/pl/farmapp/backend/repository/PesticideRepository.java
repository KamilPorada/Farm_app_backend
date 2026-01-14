package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Pesticide;

import java.util.List;

@Repository
public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {

    List<Pesticide> findByFarmerId(Integer farmerId);

    List<Pesticide> findByPesticideTypeId(Integer pesticideTypeId);
}
