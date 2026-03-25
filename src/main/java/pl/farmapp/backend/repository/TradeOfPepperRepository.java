package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.farmapp.backend.entity.TradeOfPepper;

import java.time.LocalDate;
import java.util.List;

public interface TradeOfPepperRepository
        extends JpaRepository<TradeOfPepper, Integer> {

    List<TradeOfPepper> findByFarmerId(Integer farmerId);

    List<TradeOfPepper> findByPointOfSaleId(Integer pointOfSaleId);

    List<TradeOfPepper> findByFarmerIdAndTradeDateBetween(
            Integer farmerId,
            LocalDate from,
            LocalDate to
    );
    @Query(value = """
        SELECT *
        FROM trade_of_pepper
        WHERE farmer_id = :farmerId
        AND EXTRACT(YEAR FROM trade_date) = :year
        """, nativeQuery = true)
    List<TradeOfPepper> findByFarmerIdAndYear(
            @Param("farmerId") Integer farmerId,
            @Param("year") Integer year
    );
}
