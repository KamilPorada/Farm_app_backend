package pl.farmapp.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.ExpenseCategoryDto;
import pl.farmapp.backend.entity.ExpenseCategory;
import pl.farmapp.backend.repository.ExpenseCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;

    public ExpenseCategoryService(ExpenseCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ExpenseCategoryDto> getAll(Integer farmerId) {
        return repository.findByFarmerIdOrderByNameAsc(farmerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ExpenseCategoryDto create(Integer farmerId, ExpenseCategoryDto dto) {
        if (repository.existsByFarmerIdAndNameIgnoreCase(farmerId, dto.getName())) {
            throw new IllegalArgumentException("Kategoria o tej nazwie już istnieje");
        }

        ExpenseCategory category = new ExpenseCategory();
        category.setFarmerId(farmerId);
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());

        return toDto(repository.save(category));
    }

    public ExpenseCategoryDto update(Integer id, Integer farmerId, ExpenseCategoryDto dto) {
        ExpenseCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategoria nie istnieje"));

        if (!category.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        category.setName(dto.getName());
        category.setIcon(dto.getIcon());

        return toDto(repository.save(category));
    }

    public void delete(Integer id, Integer farmerId) {
        ExpenseCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategoria nie istnieje"));

        if (!category.getFarmerId().equals(farmerId)) {
            throw new SecurityException("Brak dostępu");
        }

        repository.delete(category);
    }

    private ExpenseCategoryDto toDto(ExpenseCategory category) {
        ExpenseCategoryDto dto = new ExpenseCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        return dto;
    }
}
