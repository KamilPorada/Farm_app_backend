package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Fertilizer;

import java.util.List;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Integer> {

    List<Fertilizer> findByFarmerId(Integer farmerId);
}
