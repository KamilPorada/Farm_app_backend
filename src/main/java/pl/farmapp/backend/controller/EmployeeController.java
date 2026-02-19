package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.EmployeeDto;
import pl.farmapp.backend.entity.Employee;
import pl.farmapp.backend.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // ➜ create
    @PostMapping
    public Employee create(@RequestBody EmployeeDto dto) {
        return service.create(dto);
    }

    // ➜ update danych
    @PutMapping("/{id}")
    public Employee update(@PathVariable Integer id,
                           @RequestBody EmployeeDto dto) {
        return service.update(id, dto);
    }

    // ➜ ustawienie finish date
    @PatchMapping("/{id}/finish-date")
    public Employee setFinishDate(@PathVariable Integer id,
                                  @RequestParam LocalDate finishDate) {
        return service.setFinishDate(id, finishDate);
    }

    // ➜ delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    // ➜ lista pracowników
    @GetMapping
    public List<Employee> getByFarmerAndSeason(
            @RequestParam Integer farmerId,
            @RequestParam Integer seasonYear) {

        return service.getByFarmerAndSeason(farmerId, seasonYear);
    }
}
