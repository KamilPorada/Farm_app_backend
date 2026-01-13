package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.FarmerTunnels;

import java.util.List;

@Repository
public interface FarmerTunnelsRepository extends JpaRepository<FarmerTunnels, Integer> {
    List<FarmerTunnels> findByFarmerId(Integer farmerId);

}
