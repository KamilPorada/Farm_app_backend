package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.WorkTimeDto;
import pl.farmapp.backend.entity.WorkTime;
import pl.farmapp.backend.service.WorkTimeService;

import java.util.List;

@RestController
@RequestMapping("/api/work-time")
@RequiredArgsConstructor
public class WorkTimeController {

    private final WorkTimeService service;

    public WorkTimeController(WorkTimeService service) {
        this.service = service;
    }

    // ➜ create
    @PostMapping
    public WorkTime create(@RequestBody WorkTimeDto dto) {
        return service.create(dto);
    }

    // ➜ update
    @PutMapping("/{id}")
    public WorkTime update(@PathVariable Integer id,
                           @RequestBody WorkTimeDto dto) {
        return service.update(id, dto);
    }

    // ➜ delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    // ➜ lista wpisów pracownika
    @GetMapping("/employee/{employeeId}")
    public List<WorkTime> getByEmployee(@PathVariable Integer employeeId) {
        return service.getByEmployee(employeeId);
    }

    @PatchMapping("/{id}/hours")
    public WorkTime updateHours(
            @PathVariable Integer id,
            @RequestBody WorkTimeDto dto) {
        return service.updateHours(id, dto.getHoursWorked());
    }

    @PatchMapping("/{id}/amount")
    public WorkTime updateAmount(
            @PathVariable Integer id,
            @RequestBody WorkTimeDto dto) {
        return service.updateAmount(id, dto.getPaidAmount());
    }

}
