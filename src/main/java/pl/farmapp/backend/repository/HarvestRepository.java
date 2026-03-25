package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.farmapp.backend.entity.Harvest;

import java.time.LocalDate;
import java.util.List;

public interface HarvestRepository extends JpaRepository<Harvest, Integer> {

    List<Harvest> findByFarmerId(Integer farmerId);
    @Query(value = """
        SELECT *
        FROM harvest
        WHERE farmer_id = :farmerId
        AND EXTRACT(YEAR FROM harvest_date) = :year
    """, nativeQuery = true)
    List<Harvest> findByFarmerIdAndYear(
            @Param("farmerId") Integer farmerId,
            @Param("year") Integer year
    );


}
