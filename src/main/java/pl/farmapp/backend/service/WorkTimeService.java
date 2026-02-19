package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.WorkTimeDto;
import pl.farmapp.backend.entity.WorkTime;
import pl.farmapp.backend.repository.WorkTimeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkTimeService {

    private final WorkTimeRepository repository;

    public WorkTimeService(WorkTimeRepository repository) {
        this.repository = repository;
    }

    public WorkTime create(WorkTimeDto dto) {
        WorkTime workTime = new WorkTime();
        map(dto, workTime);
        return repository.save(workTime);
    }

    public WorkTime update(Integer id, WorkTimeDto dto) {
        WorkTime workTime = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work time entry not found"));

        map(dto, workTime);
        return repository.save(workTime);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<WorkTime> getByEmployee(Integer employeeId) {
        return repository.findByEmployeeIdOrderByWorkDateDesc(employeeId);
    }

    public WorkTime updateHours(Integer id, BigDecimal hours) {
        WorkTime entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        entity.setHoursWorked(hours);
        return repository.save(entity);
    }

    public WorkTime updateAmount(Integer id, BigDecimal amount) {
        WorkTime entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        entity.setPaidAmount(amount);
        return repository.save(entity);
    }


    private void map(WorkTimeDto dto, WorkTime entity) {
        entity.setFarmerId(dto.getFarmerId());
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setWorkDate(dto.getWorkDate());
        entity.setHoursWorked(dto.getHoursWorked());
        entity.setPaidAmount(dto.getPaidAmount());
    }
}
