package pl.farmapp.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FertilizerDto;
import pl.farmapp.backend.dto.FertilizerWithPriceDto;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.entity.FertilizerPrice;
import pl.farmapp.backend.repository.FertilizerPriceRepository;
import pl.farmapp.backend.repository.FertilizerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FertilizerService {

    private final FertilizerRepository fertilizerRepository;
    private final FertilizerPriceRepository priceRepository;

    public FertilizerService(FertilizerRepository fertilizerRepository, FertilizerPriceRepository priceRepository) {
        this.fertilizerRepository = fertilizerRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public Fertilizer create(Integer farmerId, FertilizerDto dto) {

        // znajdź lub utwórz nawóz
        Fertilizer fertilizer = fertilizerRepository
                .findByFarmerIdAndNameIgnoreCase(farmerId, dto.getName())
                .orElseGet(() -> {
                    Fertilizer f = new Fertilizer();
                    f.setFarmerId(farmerId);
                    f.setName(dto.getName());
                    f.setForm(dto.getForm());
                    return fertilizerRepository.save(f);
                });

        // zapis ceny sezonowej
        if (dto.getPrice() != null && dto.getSeasonYear() != null) {

            FertilizerPrice price = priceRepository
                    .findByFarmerIdAndFertilizerIdAndSeasonYear(
                            farmerId,
                            fertilizer.getId(),
                            dto.getSeasonYear()
                    )
                    .orElse(new FertilizerPrice());

            price.setFarmerId(farmerId);
            price.setFertilizerId(fertilizer.getId());
            price.setSeasonYear(dto.getSeasonYear());
            price.setPricePerUnit(dto.getPrice());

            priceRepository.save(price);
        }

        return fertilizer;
    }

    @Transactional
    public Fertilizer update(Integer id, FertilizerDto dto) {

        Fertilizer fertilizer = fertilizerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertilizer not found"));

        fertilizer.setName(dto.getName());
        fertilizer.setForm(dto.getForm());

        fertilizerRepository.save(fertilizer);

        // 🔥 aktualizacja ceny sezonowej
        if (dto.getSeasonYear() != null) {

            FertilizerPrice price = priceRepository
                    .findByFarmerIdAndFertilizerIdAndSeasonYear(
                            fertilizer.getFarmerId(),
                            fertilizer.getId(),
                            dto.getSeasonYear()
                    )
                    .orElse(new FertilizerPrice());

            price.setFarmerId(fertilizer.getFarmerId());
            price.setFertilizerId(fertilizer.getId());
            price.setSeasonYear(dto.getSeasonYear());

            // 🔥 pozwala zapisać NULL
            price.setPricePerUnit(dto.getPrice());

            priceRepository.save(price);
        }

        return fertilizer;
    }

    public void delete(Integer id) {
        fertilizerRepository.deleteById(id);
    }

    public List<Fertilizer> getByFarmer(Integer farmerId) {
        return fertilizerRepository.findByFarmerId(farmerId);
    }

    /**
     * 🔥 POBIERANIE NAWOZÓW Z CENĄ DLA SEZONU
     * jeśli cena nie istnieje → tworzy i kopiuje z poprzedniego roku
     */
    @Transactional
    public List<FertilizerWithPriceDto> getByFarmerAndSeason(Integer farmerId, Integer seasonYear) {

        List<Fertilizer> fertilizers = fertilizerRepository.findByFarmerId(farmerId);
        List<FertilizerWithPriceDto> result = new ArrayList<>();

        for (Fertilizer fertilizer : fertilizers) {

            FertilizerPrice price = priceRepository
                    .findByFarmerIdAndFertilizerIdAndSeasonYear(
                            farmerId,
                            fertilizer.getId(),
                            seasonYear
                    )
                    .orElseGet(() -> createPriceFromPreviousYear(
                            farmerId,
                            fertilizer.getId(),
                            seasonYear
                    ));

            result.add(new FertilizerWithPriceDto(
                    fertilizer.getId(),
                    fertilizer.getName(),
                    fertilizer.getForm(),
                    price.getPricePerUnit()
            ));
        }

        return result;
    }

    private FertilizerPrice createPriceFromPreviousYear(
            Integer farmerId,
            Integer fertilizerId,
            Integer seasonYear
    ) {
        Optional<FertilizerPrice> lastPrice =
                priceRepository.findTopByFarmerIdAndFertilizerIdOrderBySeasonYearDesc(
                        farmerId,
                        fertilizerId
                );

        FertilizerPrice newPrice = new FertilizerPrice();
        newPrice.setFarmerId(farmerId);
        newPrice.setFertilizerId(fertilizerId);
        newPrice.setSeasonYear(seasonYear);

        lastPrice.ifPresent(p -> newPrice.setPricePerUnit(p.getPricePerUnit()));

        return priceRepository.save(newPrice);
    }
}