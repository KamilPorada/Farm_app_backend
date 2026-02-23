package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.NoteDto;
import pl.farmapp.backend.entity.Note;
import pl.farmapp.backend.repository.NoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteDto create(NoteDto dto) {
        Note note = mapToEntity(dto);
        return mapToDto(noteRepository.save(note));
    }

    public NoteDto update(Integer id, NoteDto dto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setNoteDate(dto.getNoteDate());

        return mapToDto(noteRepository.save(note));
    }

    public void delete(Integer id) {
        noteRepository.deleteById(id);
    }

    public List<NoteDto> getAllByFarmer(Integer farmerId) {
        return noteRepository.findByFarmerId(farmerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<NoteDto> getByFarmerAndYear(Integer farmerId, int year) {

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        return noteRepository
                .findByFarmerIdAndNoteDateBetween(farmerId, start, end)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private NoteDto mapToDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setFarmerId(note.getFarmerId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setNoteDate(note.getNoteDate());
        return dto;
    }

    private Note mapToEntity(NoteDto dto) {
        Note note = new Note();
        note.setId(dto.getId());
        note.setFarmerId(dto.getFarmerId());
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setNoteDate(dto.getNoteDate());
        return note;
    }
}