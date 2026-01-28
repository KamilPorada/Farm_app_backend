package pl.farmapp.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FarmerTunnelsDto;
import pl.farmapp.backend.dto.FarmerTunnelsSyncRequest;
import pl.farmapp.backend.entity.FarmerTunnels;
import pl.farmapp.backend.repository.FarmerTunnelsRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FarmerTunnelsService {

    private final FarmerTunnelsRepository repository;

    public FarmerTunnelsService(FarmerTunnelsRepository repository) {
        this.repository = repository;
    }

    /* =========================
       SYNC – JEDNO ŹRÓDŁO PRAWDY
       ========================= */
    public void syncFarmerTunnels(FarmerTunnelsSyncRequest request) {

        Integer farmerId = request.getFarmerId();

        List<FarmerTunnels> existing =
                repository.findByFarmerId(farmerId);

        Map<Integer, FarmerTunnels> existingByYear =
                existing.stream()
                        .collect(Collectors.toMap(
                                FarmerTunnels::getYear,
                                Function.identity()
                        ));

        Set<Integer> yearsFromFrontend = new HashSet<>();

        /* ===== ADD + UPDATE ===== */
        for (FarmerTunnelsDto dto : request.getTunnels()) {

            yearsFromFrontend.add(dto.getYear());

            FarmerTunnels entity = existingByYear.get(dto.getYear());

            if (entity == null) {
                // ➕ CREATE
                FarmerTunnels newEntity = new FarmerTunnels();
                newEntity.setFarmerId(farmerId);
                newEntity.setYear(dto.getYear());
                newEntity.setTunnelsCount(dto.getCount());
                repository.save(newEntity);
            } else {
                // 🔁 UPDATE (tylko jeśli zmiana)
                if (entity.getTunnelsCount().compareTo(dto.getCount()) != 0) {
                    entity.setTunnelsCount(dto.getCount());
                    repository.save(entity);
                }
            }
        }

        /* ===== DELETE ===== */
        for (FarmerTunnels entity : existing) {
            if (!yearsFromFrontend.contains(entity.getYear())) {
                repository.delete(entity);
            }
        }
    }

    /* ===== GET – do wczytania na frontend ===== */
    public List<FarmerTunnelsDto> getFarmerTunnels(Integer farmerId) {
        return repository.findByFarmerId(farmerId)
                .stream()
                .sorted(Comparator.comparing(FarmerTunnels::getYear))
                .map(entity -> {
                    FarmerTunnelsDto dto = new FarmerTunnelsDto();
                    dto.setYear(entity.getYear());
                    dto.setCount(entity.getTunnelsCount());
                    return dto;
                })
                .toList();
    }
}
