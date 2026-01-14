package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.WorkTime;
import pl.farmapp.backend.repository.EmployeeRepository;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.WorkTimeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WorkTimeService {

    private final WorkTimeRepository repository;
    private final FarmerRepository farmerRepository;
    private final EmployeeRepository employeeRepository;

    public WorkTimeService(
            WorkTimeRepository repository,
            FarmerRepository farmerRepository,
            EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<WorkTime> getAll() {
        return repository.findAll();
    }

    public Optional<WorkTime> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<WorkTime> create(WorkTime workTime) {
        if (workTime.getFarmer() == null || workTime.getFarmer().getId() == null
                || !farmerRepository.existsById(workTime.getFarmer().getId())) {
            return Optional.empty();
        }

        if (workTime.getEmployee() == null || workTime.getEmployee().getId() == null
                || !employeeRepository.existsById(workTime.getEmployee().getId())) {
            return Optional.empty();
        }

        return Optional.of(repository.save(workTime));
    }

    public Optional<WorkTime> update(Integer id, WorkTime updated) {
        return repository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            if (updated.getEmployee() != null && updated.getEmployee().getId() != null) {
                if (!employeeRepository.existsById(updated.getEmployee().getId())) {
                    return Optional.empty();
                }
                existing.setEmployee(updated.getEmployee());
            }

            existing.setWorkDate(updated.getWorkDate());
            existing.setHoursWorked(updated.getHoursWorked());

            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<WorkTime> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<WorkTime> getByEmployee(Integer employeeId) {
        return repository.findByEmployeeId(employeeId);
    }
}
