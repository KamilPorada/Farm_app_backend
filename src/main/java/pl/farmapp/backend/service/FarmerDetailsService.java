package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FarmerDetailsDto;
import pl.farmapp.backend.entity.FarmerDetails;
import pl.farmapp.backend.mapper.FarmerDetailsMapper;
import pl.farmapp.backend.repository.FarmerDetailsRepository;

import java.util.Optional;

@Service
public class FarmerDetailsService {

    private final FarmerDetailsRepository repository;

    public FarmerDetailsService(FarmerDetailsRepository repository) {
        this.repository = repository;
    }

    public Optional<FarmerDetailsDto> getByFarmerId(Long farmerId) {
        return repository.findByFarmerId(farmerId)
                .map(FarmerDetailsMapper::toDto);
    }


    public FarmerDetailsDto create(FarmerDetailsDto dto) {
        if (repository.existsByFarmerId(dto.farmerId)) {
            throw new RuntimeException("Farmer details already exist");
        }
        FarmerDetails saved = repository.save(FarmerDetailsMapper.toEntity(dto));
        return FarmerDetailsMapper.toDto(saved);
    }

    public FarmerDetailsDto update(Long farmerId, FarmerDetailsDto dto) {
        FarmerDetails entity = repository.findByFarmerId(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer details not found"));

        entity.setVoivodeship(dto.voivodeship);
        entity.setDistrict(dto.district);
        entity.setCommune(dto.commune);
        entity.setLocality(dto.locality);
        entity.setFarmAreaHa(dto.farmAreaHa);
        entity.setSettlementType(dto.settlementType);
        entity.setCrops(dto.crops != null ? String.join(",", dto.crops) : null);

        return FarmerDetailsMapper.toDto(repository.save(entity));
    }

    public void delete(Long farmerId) {
        FarmerDetails entity = repository.findByFarmerId(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer details not found"));
        repository.delete(entity);
    }
}
