package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FertigationDto;
import pl.farmapp.backend.entity.Fertigation;
import pl.farmapp.backend.repository.FertigationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FertigationService {

    private final FertigationRepository fertigationRepository;

    public FertigationService(FertigationRepository fertigationRepository) {
        this.fertigationRepository = fertigationRepository;
    }

    public FertigationDto create(Integer farmerId, FertigationDto dto) {

        Fertigation fertigation = new Fertigation();
        fertigation.setFarmerId(farmerId);
        fertigation.setFertilizerId(dto.getFertilizerId());
        fertigation.setFertigationDate(dto.getFertigationDate());
        fertigation.setDose(dto.getDose());
        fertigation.setTunnelCount(dto.getTunnelCount());

        fertigation = fertigationRepository.save(fertigation);

        return mapToDto(fertigation);
    }

    public FertigationDto update(Integer id, Integer farmerId, FertigationDto dto) {

        Fertigation fertigation = fertigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertigation not found"));

        if (!fertigation.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("Access denied");
        }

        fertigation.setFertilizerId(dto.getFertilizerId());
        fertigation.setFertigationDate(dto.getFertigationDate());
        fertigation.setDose(dto.getDose());
        fertigation.setTunnelCount(dto.getTunnelCount());

        fertigation = fertigationRepository.save(fertigation);

        return mapToDto(fertigation);
    }

    public void delete(Integer id, Integer farmerId) {

        Fertigation fertigation = fertigationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fertigation not found"));

        if (!fertigation.getFarmerId().equals(farmerId)) {
            throw new RuntimeException("Access denied");
        }

        fertigationRepository.delete(fertigation);
    }

    public List<FertigationDto> getByFarmer(Integer farmerId) {

        List<Fertigation> list = fertigationRepository.findByFarmerId(farmerId);
        List<FertigationDto> result = new ArrayList<>();

        for (Fertigation f : list) {
            result.add(mapToDto(f));
        }

        return result;
    }

    public List<FertigationDto> getBySeason(Integer farmerId, Integer year) {

        List<Fertigation> list = fertigationRepository.findByFarmerId(farmerId);
        List<FertigationDto> result = new ArrayList<>();

        for (Fertigation f : list) {
            if (f.getFertigationDate().getYear() == year) {
                result.add(mapToDto(f));
            }
        }

        return result;
    }

    private FertigationDto mapToDto(Fertigation f) {

        FertigationDto dto = new FertigationDto();
        dto.setId(f.getId());
        dto.setFertilizerId(f.getFertilizerId());
        dto.setFertigationDate(f.getFertigationDate());
        dto.setDose(f.getDose());
        dto.setTunnelCount(f.getTunnelCount());

        return dto;
    }
}