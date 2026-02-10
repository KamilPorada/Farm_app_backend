package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FinancialDecreaseTypeDto;
import pl.farmapp.backend.entity.FinancialDecreaseType;
import pl.farmapp.backend.repository.FinancialDecreaseTypeRepository;

import java.util.List;

@Service
public class FinancialDecreaseTypeService {

    private final FinancialDecreaseTypeRepository repository;

    public FinancialDecreaseTypeService(
            FinancialDecreaseTypeRepository repository
    ) {
        this.repository = repository;
    }

    // CREATE
    public FinancialDecreaseTypeDto create(FinancialDecreaseTypeDto dto) {

        boolean exists = repository.existsByFarmerIdAndNameAndSeasonYear(
                dto.getFarmerId(),
                dto.getName(),
                dto.getSeasonYear()
        );

        if (exists) {
            throw new IllegalStateException(
                    "Financial decrease type already exists for this season"
            );
        }

        FinancialDecreaseType entity = new FinancialDecreaseType();
        entity.setFarmerId(dto.getFarmerId());
        entity.setName(dto.getName());
        entity.setSeasonYear(dto.getSeasonYear());

        return mapToDto(repository.save(entity));
    }

    // UPDATE
    public FinancialDecreaseTypeDto update(
            Integer id,
            FinancialDecreaseTypeDto dto
    ) {
        FinancialDecreaseType entity = repository.findById(id)
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
    public List<FinancialDecreaseTypeDto> getByFarmerAndSeason(
            Integer farmerId,
            Integer seasonYear
    ) {
        return repository
                .findByFarmerIdAndSeasonYear(farmerId, seasonYear)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ----------------------
    // MAPPING
    // ----------------------
    private FinancialDecreaseTypeDto mapToDto(
            FinancialDecreaseType entity
    ) {
        FinancialDecreaseTypeDto dto = new FinancialDecreaseTypeDto();
        dto.setId(entity.getId());
        dto.setFarmerId(entity.getFarmerId());
        dto.setName(entity.getName());
        dto.setSeasonYear(entity.getSeasonYear());
        return dto;
    }
}
