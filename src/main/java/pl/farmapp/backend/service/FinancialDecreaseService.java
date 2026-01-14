package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.FinancialDecrease;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FinancialDecreaseRepository;
import pl.farmapp.backend.repository.FinancialDecreaseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialDecreaseService {

    private final FinancialDecreaseRepository repository;
    private final FarmerRepository farmerRepository;
    private final FinancialDecreaseTypeRepository typeRepository;

    public FinancialDecreaseService(
            FinancialDecreaseRepository repository,
            FarmerRepository farmerRepository,
            FinancialDecreaseTypeRepository typeRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.typeRepository = typeRepository;
    }

    public List<FinancialDecrease> getAll() {
        return repository.findAll();
    }

    public Optional<FinancialDecrease> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<FinancialDecrease> create(FinancialDecrease decrease) {

        if (decrease.getFarmer() == null ||
                decrease.getFarmer().getId() == null ||
                !farmerRepository.existsById(decrease.getFarmer().getId())) {
            return Optional.empty();
        }

        if (decrease.getFinancialDecreaseType() == null ||
                decrease.getFinancialDecreaseType().getId() == null ||
                !typeRepository.existsById(decrease.getFinancialDecreaseType().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(decrease));
    }

    public Optional<FinancialDecrease> update(
            Integer id,
            FinancialDecrease updated) {

        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null &&
                    updated.getFarmer().getId() != null) {

                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getFinancialDecreaseType() != null &&
                    updated.getFinancialDecreaseType().getId() != null) {

                if (!typeRepository.existsById(updated.getFinancialDecreaseType().getId())) {
                    return Optional.empty();
                }
                existing.setFinancialDecreaseType(updated.getFinancialDecreaseType());
            }

            existing.setTitle(updated.getTitle());
            existing.setAmount(updated.getAmount());

            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<FinancialDecrease> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<FinancialDecrease> getByType(Integer typeId) {
        return repository.findByFinancialDecreaseTypeId(typeId);
    }
}
