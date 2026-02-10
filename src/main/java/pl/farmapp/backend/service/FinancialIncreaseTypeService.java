package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FinancialIncreaseTypeDto;
import pl.farmapp.backend.entity.FinancialIncreaseType;
import pl.farmapp.backend.repository.FinancialIncreaseTypeRepository;

import java.util.List;

@Service
public class FinancialIncreaseTypeService {

    private final FinancialIncreaseTypeRepository repository;

    public FinancialIncreaseTypeService(
            FinancialIncreaseTypeRepository repository
    ) {
        this.repository = repository;
    }

    // CREATE
    public FinancialIncreaseTypeDto create(FinancialIncreaseTypeDto dto) {

        boolean exists = repository.existsByFarmerIdAndNameAndSeasonYear(
                dto.getFarmerId(),
                dto.getName(),
                dto.getSeasonYear()
        );

        if (exists) {
            throw new IllegalStateException(
                    "Financial increase type already exists for this season"
            );
        }

        FinancialIncreaseType entity = new FinancialIncreaseType();
        entity.setFarmerId(dto.getFarmerId());
        entity.setName(dto.getName());
        entity.setSeasonYear(dto.getSeasonYear());

        return mapToDto(repository.save(entity));
    }

    // UPDATE
    public FinancialIncreaseTypeDto update(
            Integer id,
            FinancialIncreaseTypeDto dto
    ) {
        FinancialIncreaseType entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Type not found")
                );

        entity.setName(dto.getName());

        return mapToDto(repository.save(entity));
    }

    // DELETE
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // GET BY FARMER + SEASON
    public List<FinancialIncreaseTypeDto> getByFarmerAndSeason(
            Integer farmerId,
            Integer seasonYear
    ) {
        return repository
                .findByFarmerIdAndSeasonYear(farmerId, seasonYear)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // MAPPING
    private FinancialIncreaseTypeDto mapToDto(
            FinancialIncreaseType entity
    ) {
        FinancialIncreaseTypeDto dto = new FinancialIncreaseTypeDto();
        dto.setId(entity.getId());
        dto.setFarmerId(entity.getFarmerId());
        dto.setName(entity.getName());
        dto.setSeasonYear(entity.getSeasonYear());
        return dto;
    }
}
