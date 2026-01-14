package pl.farmapp.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.entity.WorkTime;
import pl.farmapp.backend.service.WorkTimeService;

import java.util.List;

@RestController
@RequestMapping("/api/work-times")
public class WorkTimeController {

    private final WorkTimeService service;

    public WorkTimeController(WorkTimeService service) {
        this.service = service;
    }

    @GetMapping
    public List<WorkTime> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkTime> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WorkTime> create(@RequestBody WorkTime workTime) {
        return service.create(workTime)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkTime> update(
            @PathVariable Integer id,
            @RequestBody WorkTime workTime) {
        return service.update(id, workTime)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/{farmerId}")
    public List<WorkTime> getByFarmer(@PathVariable Integer farmerId) {
        return service.getByFarmer(farmerId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<WorkTime> getByEmployee(@PathVariable Integer employeeId) {
        return service.getByEmployee(employeeId);
    }
}
