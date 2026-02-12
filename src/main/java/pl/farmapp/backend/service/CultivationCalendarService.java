package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.CultivationCalendarDto;
import pl.farmapp.backend.entity.CultivationCalendar;
import pl.farmapp.backend.repository.CultivationCalendarRepository;

import java.util.Optional;

@Service
public class CultivationCalendarService {

    private final CultivationCalendarRepository repository;

    public CultivationCalendarService(CultivationCalendarRepository repository) {
        this.repository = repository;
    }

    public CultivationCalendarDto create(CultivationCalendarDto dto) {
        CultivationCalendar entity = mapToEntity(dto);
        return mapToDto(repository.save(entity));
    }

    public CultivationCalendarDto update(Integer id, CultivationCalendarDto dto) {
        CultivationCalendar existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));

        existing.setPrickingStartDate(dto.getPrickingStartDate());
        existing.setPrickingEndDate(dto.getPrickingEndDate());
        existing.setPlantingStartDate(dto.getPlantingStartDate());
        existing.setPlantingEndDate(dto.getPlantingEndDate());
        existing.setHarvestStartDate(dto.getHarvestStartDate());
        existing.setHarvestEndDate(dto.getHarvestEndDate());

        return mapToDto(repository.save(existing));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Optional<CultivationCalendarDto> getByFarmerAndYear(Integer farmerId, Integer year) {
        return repository.findByFarmerIdAndSeasonYear(farmerId, year)
                .map(this::mapToDto);
    }

    private CultivationCalendar mapToEntity(CultivationCalendarDto dto) {
        CultivationCalendar entity = new CultivationCalendar();
        entity.setId(dto.getId());
        entity.setFarmerId(dto.getFarmerId());
        entity.setSeasonYear(dto.getSeasonYear());
        entity.setPrickingStartDate(dto.getPrickingStartDate());
        entity.setPrickingEndDate(dto.getPrickingEndDate());
        entity.setPlantingStartDate(dto.getPlantingStartDate());
        entity.setPlantingEndDate(dto.getPlantingEndDate());
        entity.setHarvestStartDate(dto.getHarvestStartDate());
        entity.setHarvestEndDate(dto.getHarvestEndDate());
        return entity;
    }

    private CultivationCalendarDto mapToDto(CultivationCalendar entity) {
        CultivationCalendarDto dto = new CultivationCalendarDto();
        dto.setId(entity.getId());
        dto.setFarmerId(entity.getFarmerId());
        dto.setSeasonYear(entity.getSeasonYear());
        dto.setPrickingStartDate(entity.getPrickingStartDate());
        dto.setPrickingEndDate(entity.getPrickingEndDate());
        dto.setPlantingStartDate(entity.getPlantingStartDate());
        dto.setPlantingEndDate(entity.getPlantingEndDate());
        dto.setHarvestStartDate(entity.getHarvestStartDate());
        dto.setHarvestEndDate(entity.getHarvestEndDate());
        return dto;
    }
}
