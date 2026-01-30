package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
