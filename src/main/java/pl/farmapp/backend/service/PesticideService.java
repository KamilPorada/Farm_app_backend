package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.PesticideDto;
import pl.farmapp.backend.entity.Pesticide;
import pl.farmapp.backend.repository.PesticideRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PesticideService {

    private final PesticideRepository repository;

    public PesticideService(PesticideRepository repository) {
        this.repository = repository;
    }

    public List<PesticideDto> getAll(Integer farmerId) {
        return repository.findAllByFarmerId(farmerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PesticideDto> getByType(Integer farmerId, Integer typeId) {
        return repository.findAllByFarmerIdAndPesticideTypeId(farmerId, typeId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PesticideDto create(Integer farmerId, PesticideDto dto) {
        Pesticide entity = new Pesticide();
        entity.setFarmerId(farmerId);
        entity.setPesticideTypeId(dto.getPesticideTypeId());
        entity.setName(dto.getName());
        entity.setIsLiquid(dto.getIsLiquid());
        entity.setTargetPest(dto.getTargetPest());
        entity.setCarenceDays(dto.getCarenceDays());

        return mapToDto(repository.save(entity));
    }

    public PesticideDto update(Integer id, Integer farmerId, PesticideDto dto) {
        Pesticide entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesticide not found"));

        if (!entity.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("Access denied");
        }

        entity.setPesticideTypeId(dto.getPesticideTypeId());
        entity.setName(dto.getName());
        entity.setIsLiquid(dto.getIsLiquid());
        entity.setTargetPest(dto.getTargetPest());
        entity.setCarenceDays(dto.getCarenceDays());

        return mapToDto(repository.save(entity));
    }

    public void delete(Integer id, Integer farmerId) {
        Pesticide entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pesticide not found"));

        if (!entity.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("Access denied");
        }

        repository.delete(entity);
    }

    private PesticideDto mapToDto(Pesticide entity) {
        PesticideDto dto = new PesticideDto();
        dto.setId(entity.getId());
        dto.setPesticideTypeId(entity.getPesticideTypeId());
        dto.setName(entity.getName());
        dto.setIsLiquid(entity.getIsLiquid());
        dto.setTargetPest(entity.getTargetPest());
        dto.setCarenceDays(entity.getCarenceDays());
        return dto;
    }
}
