package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Fertigation;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FertigationRepository;
import pl.farmapp.backend.repository.FertilizerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FertigationService {

    private final FertigationRepository repository;
    private final FarmerRepository farmerRepository;
    private final FertilizerRepository fertilizerRepository;

    public FertigationService(FertigationRepository repository,
                              FarmerRepository farmerRepository,
                              FertilizerRepository fertilizerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.fertilizerRepository = fertilizerRepository;
    }

    public List<Fertigation> getAll() {
        return repository.findAll();
    }

    public Optional<Fertigation> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Fertigation> create(Fertigation fertigation) {
        if (fertigation.getFarmer() == null || fertigation.getFarmer().getId() == null
                || !farmerRepository.existsById(fertigation.getFarmer().getId())) {
            return Optional.empty();
        }
        if (fertigation.getFertilizer() == null || fertigation.getFertilizer().getId() == null
                || !fertilizerRepository.existsById(fertigation.getFertilizer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(fertigation));
    }

    public Optional<Fertigation> update(Integer id, Fertigation updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            if (updated.getFertilizer() != null && updated.getFertilizer().getId() != null) {
                if (!fertilizerRepository.existsById(updated.getFertilizer().getId())) {
                    return Optional.empty();
                }
                existing.setFertilizer(updated.getFertilizer());
            }
            existing.setFertigationDate(updated.getFertigationDate());
            existing.setDose(updated.getDose());
            existing.setTunnelCount(updated.getTunnelCount());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Fertigation> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<Fertigation> getByFertilizer(Integer fertilizerId) {
        return repository.findByFertilizerId(fertilizerId);
    }
}
