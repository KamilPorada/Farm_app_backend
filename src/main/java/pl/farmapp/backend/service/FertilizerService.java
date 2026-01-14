package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FertilizerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FertilizerService {

    private final FertilizerRepository repository;
    private final FarmerRepository farmerRepository;

    public FertilizerService(FertilizerRepository repository, FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<Fertilizer> getAll() {
        return repository.findAll();
    }

    public Optional<Fertilizer> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Fertilizer> create(Fertilizer fertilizer) {
        if (fertilizer.getFarmer() == null || fertilizer.getFarmer().getId() == null
                || !farmerRepository.existsById(fertilizer.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(fertilizer));
    }

    public Optional<Fertilizer> update(Integer id, Fertilizer updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            existing.setName(updated.getName());
            existing.setIsLiquid(updated.getIsLiquid());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Fertilizer> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
