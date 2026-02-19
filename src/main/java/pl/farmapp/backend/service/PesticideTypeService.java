package pl.farmapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.PesticideTypeDto;
import pl.farmapp.backend.entity.PesticideType;
import pl.farmapp.backend.repository.PesticideTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PesticideTypeService {

    private final PesticideTypeRepository repository;

    public PesticideTypeService(PesticideTypeRepository repository) {
        this.repository = repository;
    }

    public List<PesticideTypeDto> getAll(Integer farmerId) {
        return repository.findByFarmerIdOrderByNameAsc(farmerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PesticideTypeDto create(Integer farmerId, PesticideTypeDto dto) {
        if (repository.existsByFarmerIdAndNameIgnoreCase(farmerId, dto.getName())) {
            throw new IllegalArgumentException("Typ o tej nazwie już istnieje");
        }

        PesticideType type = new PesticideType();
        type.setFarmerId(farmerId);
        type.setName(dto.getName());
        type.setIcon(dto.getIcon());

        return toDto(repository.save(type));
    }

    public PesticideTypeDto update(Integer id, Integer farmerId, PesticideTypeDto dto) {
        PesticideType type = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Typ nie istnieje"));

        if (!type.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        type.setName(dto.getName());
        type.setIcon(dto.getIcon());

        return toDto(repository.save(type));
    }

    public void delete(Integer id, Integer farmerId) {
        PesticideType type = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Typ nie istnieje"));

        if (!type.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        repository.delete(type);
    }

    private PesticideTypeDto toDto(PesticideType type) {
        PesticideTypeDto dto = new PesticideTypeDto();
        dto.setId(type.getId());
        dto.setName(type.getName());
        dto.setIcon(type.getIcon());
        return dto;
    }
}
