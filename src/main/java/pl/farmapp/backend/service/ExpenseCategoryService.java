package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.ExpenseCategory;
import pl.farmapp.backend.repository.ExpenseCategoryRepository;
import pl.farmapp.backend.repository.FarmerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final FarmerRepository farmerRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository,
                                  FarmerRepository farmerRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.farmerRepository = farmerRepository;
    }

    public List<ExpenseCategory> getAll() {
        return expenseCategoryRepository.findAll();
    }

    public Optional<ExpenseCategory> getById(Integer id) {
        return expenseCategoryRepository.findById(id);
    }

    public Optional<ExpenseCategory> create(ExpenseCategory category) {

        if (category.getFarmer() == null ||
                category.getFarmer().getId() == null ||
                !farmerRepository.existsById(category.getFarmer().getId())) {
            return Optional.empty();
        }

        return Optional.of(expenseCategoryRepository.save(category));
    }

    public Optional<ExpenseCategory> update(Integer id, ExpenseCategory updated) {
        return expenseCategoryRepository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            existing.setName(updated.getName());

            return Optional.of(expenseCategoryRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        expenseCategoryRepository.deleteById(id);
    }

    public List<ExpenseCategory> getByFarmer(Integer farmerId) {
        return expenseCategoryRepository.findByFarmerId(farmerId);
    }
}
