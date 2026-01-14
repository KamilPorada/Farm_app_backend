package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.FinancialIncreaseType;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FinancialIncreaseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialIncreaseTypeService {

    private final FinancialIncreaseTypeRepository repository;
    private final FarmerRepository farmerRepository;

    public FinancialIncreaseTypeService(
            FinancialIncreaseTypeRepository repository,
            FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<FinancialIncreaseType> getAll() {
        return repository.findAll();
    }

    public Optional<FinancialIncreaseType> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<FinancialIncreaseType> create(FinancialIncreaseType type) {

        if (type.getFarmer() == null ||
                type.getFarmer().getId() == null ||
                !farmerRepository.existsById(type.getFarmer().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(type));
    }

    public Optional<FinancialIncreaseType> update(
            Integer id,
            FinancialIncreaseType updated) {

        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null &&
                    updated.getFarmer().getId() != null) {

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

    public List<FinancialIncreaseType> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
