package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.farmapp.backend.entity.PointOfSale;

import java.util.List;

@Repository
public interface PointOfSaleRepository extends JpaRepository<PointOfSale, Integer> {

    List<PointOfSale> findByFarmerId(Integer farmerId);
}
