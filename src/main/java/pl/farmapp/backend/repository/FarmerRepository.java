package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.Farmer;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer> {

}
