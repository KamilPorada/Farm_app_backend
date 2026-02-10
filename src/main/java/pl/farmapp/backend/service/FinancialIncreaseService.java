package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FinancialIncreaseDto;
import pl.farmapp.backend.entity.FinancialIncrease;
import pl.farmapp.backend.entity.FinancialIncreaseType;
import pl.farmapp.backend.repository.FinancialIncreaseRepository;
import pl.farmapp.backend.repository.FinancialIncreaseTypeRepository;

import java.util.List;

@Service
public class FinancialIncreaseService {

    private final FinancialIncreaseRepository repository;
    private final FinancialIncreaseTypeRepository typeRepository;

    public FinancialIncreaseService(
            FinancialIncreaseRepository repository,
            FinancialIncreaseTypeRepository typeRepository
    ) {
        this.repository = repository;
        this.typeRepository = typeRepository;
    }

    // CREATE
    public FinancialIncreaseDto create(FinancialIncreaseDto dto) {

        FinancialIncreaseType type = typeRepository
                .findById(dto.getTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Type not found")
                );

        FinancialIncrease entity = new FinancialIncrease();
        entity.setFarmerId(dto.getFarmerId());
        entity.setType(type);
        entity.setTitle(dto.getTitle());
        entity.setAmount(dto.getAmount());

        return mapToDto(repository.save(entity));
    }

    // UPDATE
    public FinancialIncreaseDto update(Integer id, FinancialIncreaseDto dto) {
        FinancialIncrease entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Increase not found"));

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
    public List<FinancialIncreaseDto> getByFarmerAndSeason(
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

    // GET BY FARMER + SEASON + TYPE
    public List<FinancialIncreaseDto> getByFarmerSeasonAndType(
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

    // MAPPING
    private FinancialIncreaseDto mapToDto(
            FinancialIncrease entity
    ) {
        FinancialIncreaseDto dto = new FinancialIncreaseDto();
        dto.setId(entity.getId());
        dto.setFarmerId(entity.getFarmerId());
        dto.setTypeId(entity.getType().getId());
        dto.setTypeName(entity.getType().getName());
        dto.setTitle(entity.getTitle());
        dto.setAmount(entity.getAmount());
        return dto;
    }
}
