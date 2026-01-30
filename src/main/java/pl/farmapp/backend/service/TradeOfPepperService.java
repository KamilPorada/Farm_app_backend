package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.TradeOfPepperCreateDto;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.entity.PointOfSale;
import pl.farmapp.backend.entity.TradeOfPepper;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PointOfSaleRepository;
import pl.farmapp.backend.repository.TradeOfPepperRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeOfPepperService {

    private final TradeOfPepperRepository tradeRepo;
    private final FarmerRepository farmerRepo;
    private final PointOfSaleRepository posRepo;

    public TradeOfPepperService(
            TradeOfPepperRepository tradeRepo,
            FarmerRepository farmerRepo,
            PointOfSaleRepository posRepo) {
        this.tradeRepo = tradeRepo;
        this.farmerRepo = farmerRepo;
        this.posRepo = posRepo;
    }

    public List<TradeOfPepper> getAll() {
        return tradeRepo.findAll();
    }

    public Optional<TradeOfPepper> getById(Integer id) {
        return tradeRepo.findById(id);
    }

    public List<TradeOfPepper> getByFarmer(Integer farmerId) {
        return tradeRepo.findByFarmerId(farmerId);
    }

    public List<TradeOfPepper> getByFarmerAndYear(Integer farmerId, int year) {
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);
        return tradeRepo.findByFarmerIdAndTradeDateBetween(farmerId, from, to);
    }

    public Optional<TradeOfPepper> create(TradeOfPepperCreateDto dto) {

        Farmer farmer = farmerRepo.findById(dto.farmerId).orElse(null);
        PointOfSale pos = posRepo.findById(dto.pointOfSaleId).orElse(null);

        if (farmer == null || pos == null) return Optional.empty();
        if (dto.tradePrice.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        if (dto.tradeWeight.compareTo(BigDecimal.ZERO) <= 0) return Optional.empty();
        if (dto.vatRate < 0 || dto.vatRate > 100) return Optional.empty();

        TradeOfPepper e = new TradeOfPepper();
        e.setFarmer(farmer);
        e.setPointOfSale(pos);
        e.setTradeDate(dto.tradeDate);
        e.setPepperClass(dto.pepperClass);
        e.setPepperColor(dto.pepperColor);
        e.setTradePrice(dto.tradePrice);
        e.setTradeWeight(dto.tradeWeight);
        e.setVatRate(dto.vatRate);

        return Optional.of(tradeRepo.save(e));
    }

    public Optional<TradeOfPepper> update(Integer id, TradeOfPepperCreateDto dto) {
        return tradeRepo.findById(id).flatMap(existing -> {

            Farmer farmer = farmerRepo.findById(dto.farmerId).orElse(null);
            PointOfSale pos = posRepo.findById(dto.pointOfSaleId).orElse(null);

            if (farmer == null || pos == null) return Optional.empty();

            existing.setFarmer(farmer);
            existing.setPointOfSale(pos);
            existing.setTradeDate(dto.tradeDate);
            existing.setPepperClass(dto.pepperClass);
            existing.setPepperColor(dto.pepperColor);
            existing.setTradePrice(dto.tradePrice);
            existing.setTradeWeight(dto.tradeWeight);
            existing.setVatRate(dto.vatRate);

            return Optional.of(tradeRepo.save(existing));
        });
    }

    public void delete(Integer id) {
        tradeRepo.deleteById(id);
    }
}
