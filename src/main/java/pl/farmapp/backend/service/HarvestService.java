package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.HarvestDto;
import pl.farmapp.backend.entity.Harvest;
import pl.farmapp.backend.repository.HarvestRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestService {

    private final HarvestRepository harvestRepository;

    public HarvestService(HarvestRepository harvestRepository) {
        this.harvestRepository = harvestRepository;
    }

    public HarvestDto create(Integer farmerId, HarvestDto dto) {

        Harvest harvest = new Harvest();
        harvest.setFarmerId(farmerId);
        harvest.setVarietySeasonId(dto.getVarietySeasonId());
        harvest.setHarvestDate(dto.getHarvestDate());
        harvest.setBoxCount(dto.getBoxCount());

        Harvest saved = harvestRepository.save(harvest);

        return mapToDto(saved);
    }

    public HarvestDto update(Integer id, HarvestDto dto) {

        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));

        harvest.setVarietySeasonId(dto.getVarietySeasonId());
        harvest.setHarvestDate(dto.getHarvestDate());
        harvest.setBoxCount(dto.getBoxCount());

        Harvest updated = harvestRepository.save(harvest);

        return mapToDto(updated);
    }

    public void delete(Integer id) {
        harvestRepository.deleteById(id);
    }

    public List<HarvestDto> getByFarmerAndYear(Integer farmerId, Integer year) {

        return harvestRepository
                .findByFarmerId(farmerId)
                .stream()
                .filter(h -> h.getHarvestDate() != null
                        && h.getHarvestDate().getYear() == year)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private HarvestDto mapToDto(Harvest harvest) {

        HarvestDto dto = new HarvestDto();
        dto.setId(harvest.getId());
        dto.setVarietySeasonId(harvest.getVarietySeasonId());
        dto.setHarvestDate(harvest.getHarvestDate());
        dto.setBoxCount(harvest.getBoxCount());

        return dto;
    }
}
