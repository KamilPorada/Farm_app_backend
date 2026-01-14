package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Harvest;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.HarvestRepository;
import pl.farmapp.backend.repository.VarietySeasonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HarvestService {

    private final HarvestRepository repository;
    private final FarmerRepository farmerRepository;
    private final VarietySeasonRepository varietySeasonRepository;

    public HarvestService(
            HarvestRepository repository,
            FarmerRepository farmerRepository,
            VarietySeasonRepository varietySeasonRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.varietySeasonRepository = varietySeasonRepository;
    }

    public List<Harvest> getAll() {
        return repository.findAll();
    }

    public Optional<Harvest> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Harvest> create(Harvest harvest) {

        if (harvest.getFarmer() == null
                || harvest.getFarmer().getId() == null
                || !farmerRepository.existsById(harvest.getFarmer().getId())) {
            return Optional.empty();
        }

        if (harvest.getVarietySeason() == null
                || harvest.getVarietySeason().getId() == null
                || !varietySeasonRepository.existsById(harvest.getVarietySeason().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(harvest));
    }

    public Optional<Harvest> update(Integer id, Harvest updated) {
        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getVarietySeason() != null && updated.getVarietySeason().getId() != null) {
                if (!varietySeasonRepository.existsById(updated.getVarietySeason().getId())) {
                    return Optional.empty();
                }
                existing.setVarietySeason(updated.getVarietySeason());
            }

            existing.setHarvestDate(updated.getHarvestDate());
            existing.setBoxCount(updated.getBoxCount());

            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Harvest> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<Harvest> getByVarietySeason(Integer varietySeasonId) {
        return repository.findByVarietySeasonId(varietySeasonId);
    }
}
