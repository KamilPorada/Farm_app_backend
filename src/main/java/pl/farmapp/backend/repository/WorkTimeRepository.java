package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.WorkTime;

import java.util.List;

public interface WorkTimeRepository extends JpaRepository<WorkTime, Integer> {

    List<WorkTime> findByFarmerId(Integer farmerId);

    List<WorkTime> findByEmployeeId(Integer employeeId);
}
