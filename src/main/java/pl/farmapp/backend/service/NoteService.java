package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Note;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.NoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository repository;
    private final FarmerRepository farmerRepository;

    public NoteService(NoteRepository repository, FarmerRepository farmerRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
    }

    public List<Note> getAll() {
        return repository.findAll();
    }

    public Optional<Note> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Note> create(Note note) {
        if (note.getFarmer() == null || note.getFarmer().getId() == null
                || !farmerRepository.existsById(note.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(note));
    }

    public Optional<Note> update(Integer id, Note updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            existing.setTitle(updated.getTitle());
            existing.setContent(updated.getContent());
            existing.setNoteDate(updated.getNoteDate());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Note> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }
}
