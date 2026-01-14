package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.FinancialDecreaseType;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FinancialDecreaseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialDecreaseTypeService {

    private final FinancialDecreaseTypeRepository repository;
    private final FarmerRepository farmerRepository;

    public FinancialDecreaseTypeService(
            FinancialDecreaseTypeRepository repository,
            FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<FinancialDecreaseType> getAll() {
        return repository.findAll();
    }

    public Optional<FinancialDecreaseType> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<FinancialDecreaseType> create(FinancialDecreaseType type) {

        if (type.getFarmer() == null ||
                type.getFarmer().getId() == null ||
                !farmerRepository.existsById(type.getFarmer().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(type));
    }

    public Optional<FinancialDecreaseType> update(
            Integer id,
            FinancialDecreaseType updated) {

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

    public List<FinancialDecreaseType> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
