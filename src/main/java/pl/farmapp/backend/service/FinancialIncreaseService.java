package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.FinancialIncrease;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FinancialIncreaseRepository;
import pl.farmapp.backend.repository.FinancialIncreaseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialIncreaseService {

    private final FinancialIncreaseRepository repository;
    private final FarmerRepository farmerRepository;
    private final FinancialIncreaseTypeRepository typeRepository;

    public FinancialIncreaseService(
            FinancialIncreaseRepository repository,
            FarmerRepository farmerRepository,
            FinancialIncreaseTypeRepository typeRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.typeRepository = typeRepository;
    }

    public List<FinancialIncrease> getAll() {
        return repository.findAll();
    }

    public Optional<FinancialIncrease> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<FinancialIncrease> create(FinancialIncrease increase) {

        if (increase.getFarmer() == null ||
                increase.getFarmer().getId() == null ||
                !farmerRepository.existsById(increase.getFarmer().getId())) {
            return Optional.empty();
        }

        if (increase.getFinancialIncreaseType() == null ||
                increase.getFinancialIncreaseType().getId() == null ||
                !typeRepository.existsById(increase.getFinancialIncreaseType().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(increase));
    }

    public Optional<FinancialIncrease> update(
            Integer id,
            FinancialIncrease updated) {

        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null &&
                    updated.getFarmer().getId() != null) {

                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getFinancialIncreaseType() != null &&
                    updated.getFinancialIncreaseType().getId() != null) {

                if (!typeRepository.existsById(updated.getFinancialIncreaseType().getId())) {
                    return Optional.empty();
                }
                existing.setFinancialIncreaseType(updated.getFinancialIncreaseType());
            }

            existing.setTitle(updated.getTitle());
            existing.setAmount(updated.getAmount());

            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<FinancialIncrease> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<FinancialIncrease> getByType(Integer typeId) {
        return repository.findByFinancialIncreaseTypeId(typeId);
    }
}
