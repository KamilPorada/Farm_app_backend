package pl.farmapp.backend.service;


import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FinancialDecreaseDto;
import pl.farmapp.backend.entity.FinancialDecrease;
import pl.farmapp.backend.entity.FinancialDecreaseType;
import pl.farmapp.backend.repository.FinancialDecreaseRepository;
import pl.farmapp.backend.repository.FinancialDecreaseTypeRepository;

import java.util.List;

@Service
public class FinancialDecreaseService {

    private final FinancialDecreaseRepository repository;
    private final FinancialDecreaseTypeRepository typeRepository;

    public FinancialDecreaseService(
            FinancialDecreaseRepository repository,
            FinancialDecreaseTypeRepository typeRepository
    ) {
        this.repository = repository;
        this.typeRepository = typeRepository;
    }

    // CREATE
    public FinancialDecreaseDto create(FinancialDecreaseDto dto) {

        FinancialDecreaseType type = typeRepository
                .findById(dto.getTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Type not found")
                );

        FinancialDecrease entity = new FinancialDecrease();
        entity.setFarmerId(dto.getFarmerId());
        entity.setType(type);
        entity.setTitle(dto.getTitle());
        entity.setAmount(dto.getAmount());

        return mapToDto(repository.save(entity));
    }

    // UPDATE
    public FinancialDecreaseDto update(
            Integer id,
            FinancialDecreaseDto dto
    ) {
        FinancialDecrease entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Decrease not found")
                );

        // 🔒 NIE ruszamy title, farmerId, type
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }

        return mapToDto(repository.save(entity));
    }

    // DELETE
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // GET BY FARMER + SEASON
    public List<FinancialDecreaseDto> getByFarmerAndSeason(
            Integer farmerId,
            Integer seasonYear
    ) {
        return repository
                .findByFarmerIdAndType_SeasonYear(
                        farmerId,
                        seasonYear
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    public List<FinancialDecreaseDto> getByFarmerSeasonAndType(
            Integer farmerId,
            Integer seasonYear,
            Integer typeId
    ) {
        return repository
                .findByFarmerIdAndType_IdAndType_SeasonYear(
                        farmerId,
                        typeId,
                        seasonYear
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }



    // ----------------------
    // MAPPING
    // ----------------------
    private FinancialDecreaseDto mapToDto(
            FinancialDecrease entity
    ) {
        FinancialDecreaseDto dto = new FinancialDecreaseDto();
        dto.setId(entity.getId());
        dto.setFarmerId(entity.getFarmerId());
        dto.setTypeId(entity.getType().getId());
        dto.setTypeName(entity.getType().getName());
        dto.setTitle(entity.getTitle());
        dto.setAmount(entity.getAmount());
        return dto;
    }
}
