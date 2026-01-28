package pl.farmapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.farmapp.backend.dto.AppSettingsDto;
import pl.farmapp.backend.dto.AppSettingsDto;
import pl.farmapp.backend.entity.AppSettings;
import pl.farmapp.backend.repository.AppSettingsRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final AppSettingsRepository repository;

    public AppSettingsService(AppSettingsRepository repository) {
        this.repository = repository;
    }

    /* ===== GET ===== */
    public Optional<AppSettingsDto> getByFarmerId(Long farmerId) {
        return repository.findByFarmerId(farmerId)
                .map(this::mapToDto);
    }


    /* ===== CREATE ===== */
    @Transactional
    public AppSettingsDto create(AppSettingsDto dto) {
        if (repository.existsByFarmerId(dto.getFarmerId())) {
            throw new RuntimeException("App settings already exist");
        }

        AppSettings settings = mapToEntity(dto);
        repository.save(settings);

        return mapToDto(settings);
    }

    /* ===== UPDATE ===== */
    @Transactional
    public AppSettingsDto update(Long farmerId, AppSettingsDto dto) {
        AppSettings settings = repository.findByFarmerId(farmerId)
                .orElseThrow(() -> new RuntimeException("App settings not found"));

        settings.setLanguage(dto.getLanguage());
        settings.setWeightUnit(dto.getWeightUnit());
        settings.setCurrency(dto.getCurrency());
        settings.setDateFormat(dto.getDateFormat());
        settings.setUseThousandsSeparator(dto.getUseThousandsSeparator());
        settings.setBoxWeightKg(dto.getBoxWeightKg());
        settings.setNotificationsEnabled(dto.getNotificationsEnabled());

        return mapToDto(settings);
    }

    /* ===== MAPPERY ===== */

    private AppSettingsDto mapToDto(AppSettings s) {
        AppSettingsDto dto = new AppSettingsDto();

        dto.setFarmerId(s.getFarmerId());
        dto.setLanguage(s.getLanguage());
        dto.setWeightUnit(s.getWeightUnit());
        dto.setCurrency(s.getCurrency());
        dto.setDateFormat(s.getDateFormat());
        dto.setUseThousandsSeparator(s.getUseThousandsSeparator());
        dto.setBoxWeightKg(s.getBoxWeightKg());
        dto.setNotificationsEnabled(s.getNotificationsEnabled());

        return dto;
    }


    private AppSettings mapToEntity(AppSettingsDto dto) {
        AppSettings entity = new AppSettings();

        entity.setFarmerId(dto.getFarmerId());
        entity.setLanguage(dto.getLanguage());
        entity.setWeightUnit(dto.getWeightUnit());
        entity.setCurrency(dto.getCurrency());
        entity.setDateFormat(dto.getDateFormat());
        entity.setUseThousandsSeparator(dto.getUseThousandsSeparator());
        entity.setBoxWeightKg(dto.getBoxWeightKg());
        entity.setNotificationsEnabled(dto.getNotificationsEnabled());

        return entity;
    }

}
