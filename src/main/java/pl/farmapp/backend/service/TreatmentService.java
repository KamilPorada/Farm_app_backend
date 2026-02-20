package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.TreatmentDto;
import pl.farmapp.backend.entity.Treatment;
import pl.farmapp.backend.repository.TreatmentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final TreatmentRepository repository;

    public TreatmentService(TreatmentRepository repository) {
        this.repository = repository;
    }

    public TreatmentDto create(TreatmentDto dto) {
        Treatment saved = repository.save(mapToEntity(dto));
        return mapToDto(saved);
    }

    public TreatmentDto update(Integer id, TreatmentDto dto) {
        Treatment treatment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found"));

        treatment.setFarmerId(dto.getFarmerId());
        treatment.setPesticideId(dto.getPesticideId());
        treatment.setTreatmentDate(dto.getTreatmentDate());
        treatment.setTreatmentTime(dto.getTreatmentTime());
        treatment.setPesticideDose(dto.getPesticideDose());
        treatment.setLiquidVolume(dto.getLiquidVolume());
        treatment.setTunnelCount(dto.getTunnelCount());

        Treatment updated = repository.save(treatment);
        return mapToDto(updated);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<TreatmentDto> getByFarmerAndYear(Integer farmerId, Integer year) {

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        return repository
                .findAllByFarmerIdAndTreatmentDateBetween(farmerId, start, end)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    private Treatment mapToEntity(TreatmentDto dto) {
        Treatment t = new Treatment();
        t.setId(dto.getId());
        t.setFarmerId(dto.getFarmerId());
        t.setPesticideId(dto.getPesticideId());
        t.setTreatmentDate(dto.getTreatmentDate());
        t.setTreatmentTime(dto.getTreatmentTime());
        t.setPesticideDose(dto.getPesticideDose());
        t.setLiquidVolume(dto.getLiquidVolume());
        t.setTunnelCount(dto.getTunnelCount());
        return t;
    }


    private TreatmentDto mapToDto(Treatment treatment) {
        TreatmentDto dto = new TreatmentDto();
        dto.setId(treatment.getId());
        dto.setFarmerId(treatment.getFarmerId());
        dto.setPesticideId(treatment.getPesticideId());
        dto.setTreatmentDate(treatment.getTreatmentDate());
        dto.setTreatmentTime(treatment.getTreatmentTime());
        dto.setPesticideDose(treatment.getPesticideDose());
        dto.setLiquidVolume(treatment.getLiquidVolume());
        dto.setTunnelCount(treatment.getTunnelCount());
        return dto;
    }

}
