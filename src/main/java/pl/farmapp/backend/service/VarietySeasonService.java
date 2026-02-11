package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.VarietySeasonDto;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.entity.VarietySeason;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.VarietySeasonRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VarietySeasonService {

    private final VarietySeasonRepository varietySeasonRepository;
    private final FarmerRepository farmerRepository;

    public VarietySeasonService(VarietySeasonRepository varietySeasonRepository, FarmerRepository farmerRepository) {
        this.varietySeasonRepository = varietySeasonRepository;
        this.farmerRepository = farmerRepository;
    }

    public VarietySeasonDto create(Integer farmerId, VarietySeasonDto dto) {

        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        VarietySeason variety = new VarietySeason();
        variety.setFarmer(farmer);
        variety.setSeasonYear(dto.getSeasonYear());
        variety.setName(dto.getName());
        variety.setTunnelCount(dto.getTunnelCount());
        variety.setColor(dto.getColor());

        VarietySeason saved = varietySeasonRepository.save(variety);

        return mapToDto(saved);
    }

    public VarietySeasonDto update(Integer id, VarietySeasonDto dto) {

        VarietySeason variety = varietySeasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variety not found"));

        variety.setName(dto.getName());
        variety.setSeasonYear(dto.getSeasonYear());
        variety.setTunnelCount(dto.getTunnelCount());
        variety.setColor(dto.getColor());

        VarietySeason updated = varietySeasonRepository.save(variety);

        return mapToDto(updated);
    }

    public void delete(Integer id) {
        varietySeasonRepository.deleteById(id);
    }

    public List<VarietySeasonDto> getByFarmerAndSeason(Integer farmerId, Integer seasonYear) {
        return varietySeasonRepository
                .findByFarmerIdAndSeasonYear(farmerId, seasonYear)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private VarietySeasonDto mapToDto(VarietySeason variety) {

        VarietySeasonDto dto = new VarietySeasonDto();
        dto.setId(variety.getId());
        dto.setSeasonYear(variety.getSeasonYear());
        dto.setName(variety.getName());
        dto.setTunnelCount(variety.getTunnelCount());
        dto.setColor(variety.getColor());

        return dto;
    }

}
