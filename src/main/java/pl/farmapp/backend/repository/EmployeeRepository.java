package pl.farmapp.backend.repository;

import pl.farmapp.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFarmerIdAndSeasonYear(Integer farmerId, Integer seasonYear);
}
