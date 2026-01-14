package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.VarietySeason;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.VarietySeasonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VarietySeasonService {

    private final VarietySeasonRepository repository;
    private final FarmerRepository farmerRepository;

    public VarietySeasonService(
            VarietySeasonRepository repository,
            FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<VarietySeason> getAll() {
        return repository.findAll();
    }

    public Optional<VarietySeason> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<VarietySeason> create(VarietySeason varietySeason) {
        if (varietySeason.getFarmer() == null
                || varietySeason.getFarmer().getId() == null
                || !farmerRepository.existsById(varietySeason.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(varietySeason));
    }

    public Optional<VarietySeason> update(Integer id, VarietySeason updated) {
        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            existing.setName(updated.getName());
            existing.setTunnelCount(updated.getTunnelCount());
            existing.setSeasonYear(updated.getSeasonYear());

            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<VarietySeason> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<VarietySeason> getBySeasonYear(Integer seasonYear) {
        return repository.findBySeasonYear(seasonYear);
    }
}
