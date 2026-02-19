package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.EmployeeDto;
import pl.farmapp.backend.entity.Employee;
import pl.farmapp.backend.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee create(EmployeeDto dto) {
        Employee employee = new Employee();
        map(dto, employee);
        return repository.save(employee);
    }

    public Employee update(Integer id, EmployeeDto dto) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        map(dto, employee);
        return repository.save(employee);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Employee> getByFarmerAndSeason(Integer farmerId, Integer seasonYear) {
        return repository.findByFarmerIdAndSeasonYear(farmerId, seasonYear);
    }

    public Employee setFinishDate(Integer id, LocalDate finishDate) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setFinishDate(finishDate);
        return repository.save(employee);
    }

    private void map(EmployeeDto dto, Employee employee) {
        employee.setFarmerId(dto.getFarmerId());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setNationality(dto.getNationality());
        employee.setAge(dto.getAge());
        employee.setSalary(dto.getSalary());
        employee.setStartDate(dto.getStartDate());
        employee.setSeasonYear(dto.getSeasonYear());
    }
}
