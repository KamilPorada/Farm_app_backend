package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.TradeOfPepper;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PointOfSaleRepository;
import pl.farmapp.backend.repository.TradeOfPepperRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TradeOfPepperService {

    private final TradeOfPepperRepository tradeRepository;
    private final FarmerRepository farmerRepository;
    private final PointOfSaleRepository pointOfSaleRepository;

    public TradeOfPepperService(TradeOfPepperRepository tradeRepository,
                        FarmerRepository farmerRepository,
                        PointOfSaleRepository pointOfSaleRepository) {
        this.tradeRepository = tradeRepository;
        this.farmerRepository = farmerRepository;
        this.pointOfSaleRepository = pointOfSaleRepository;
    }

    public List<TradeOfPepper> getAll() {
        return tradeRepository.findAll();
    }

    public Optional<TradeOfPepper> getById(Integer id) {
        return tradeRepository.findById(id);
    }

    public Optional<TradeOfPepper> create(TradeOfPepper trade) {

        if (trade.getFarmer() == null ||
                trade.getFarmer().getId() == null ||
                !farmerRepository.existsById(trade.getFarmer().getId())) {
            return Optional.empty();
        }

        if (trade.getPointOfSale() == null ||
                trade.getPointOfSale().getId() == null ||
                !pointOfSaleRepository.existsById(trade.getPointOfSale().getId())) {
            return Optional.empty();
        }

        return Optional.of(tradeRepository.save(trade));
    }

    public Optional<TradeOfPepper> update(Integer id, TradeOfPepper updated) {
        return tradeRepository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getPointOfSale() != null && updated.getPointOfSale().getId() != null) {
                if (!pointOfSaleRepository.existsById(updated.getPointOfSale().getId())) {
                    return Optional.empty();
                }
                existing.setPointOfSale(updated.getPointOfSale());
            }

            existing.setTradeDate(updated.getTradeDate());
            existing.setPepperClass(updated.getPepperClass());
            existing.setPepperColor(updated.getPepperColor());
            existing.setTradePrice(updated.getTradePrice());
            existing.setTradeWeight(updated.getTradeWeight());
            existing.setVatRate(updated.getVatRate());

            return Optional.of(tradeRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        tradeRepository.deleteById(id);
    }

    public List<TradeOfPepper> getByFarmer(Integer farmerId) {
        return tradeRepository.findByFarmerId(farmerId);
    }

    public List<TradeOfPepper> getByPointOfSale(Integer posId) {
        return tradeRepository.findByPointOfSaleId(posId);
    }
}
