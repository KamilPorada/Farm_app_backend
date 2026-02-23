package pl.farmapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.farmapp.backend.entity.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    List<Note> findByFarmerId(Integer farmerId);

    List<Note> findByFarmerIdAndNoteDateBetween(
            Integer farmerId,
            java.time.LocalDate start,
            java.time.LocalDate end
    );
}