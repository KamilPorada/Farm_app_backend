package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Employee;
import pl.farmapp.backend.repository.EmployeeRepository;
import pl.farmapp.backend.repository.FarmerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final FarmerRepository farmerRepository;

    public EmployeeService(EmployeeRepository repository, FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Optional<Employee> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Employee> create(Employee employee) {
        if (employee.getFarmer() == null || employee.getFarmer().getId() == null
                || !farmerRepository.existsById(employee.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(employee));
    }

    public Optional<Employee> update(Integer id, Employee updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setNationality(updated.getNationality());
            existing.setAge(updated.getAge());
            existing.setSalary(updated.getSalary());
            existing.setStartDate(updated.getStartDate());
            existing.setFinishDate(updated.getFinishDate());
            existing.setSeasonYear(updated.getSeasonYear());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Employee> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
