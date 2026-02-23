package pl.farmapp.backend.controller;

import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.NoteDto;
import pl.farmapp.backend.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public NoteDto create(@RequestBody NoteDto dto) {
        return noteService.create(dto);
    }

    @PutMapping("/{id}")
    public NoteDto update(@PathVariable Integer id, @RequestBody NoteDto dto) {
        return noteService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        noteService.delete(id);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<NoteDto> getAllByFarmer(@PathVariable Integer farmerId) {
        return noteService.getAllByFarmer(farmerId);
    }

    @GetMapping("/farmer/{farmerId}/year/{year}")
    public List<NoteDto> getByFarmerAndYear(
            @PathVariable Integer farmerId,
            @PathVariable int year
    ) {
        return noteService.getByFarmerAndYear(farmerId, year);
    }
}