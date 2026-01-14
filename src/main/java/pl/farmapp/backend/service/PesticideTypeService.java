package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.PesticideType;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PesticideTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PesticideTypeService {

    private final PesticideTypeRepository repository;
    private final FarmerRepository farmerRepository;

    public PesticideTypeService(PesticideTypeRepository repository, FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<PesticideType> getAll() {
        return repository.findAll();
    }

    public Optional<PesticideType> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<PesticideType> create(PesticideType pesticideType) {
        if (pesticideType.getFarmer() == null || pesticideType.getFarmer().getId() == null
                || !farmerRepository.existsById(pesticideType.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(pesticideType));
    }

    public Optional<PesticideType> update(Integer id, PesticideType updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            existing.setName(updated.getName());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<PesticideType> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
