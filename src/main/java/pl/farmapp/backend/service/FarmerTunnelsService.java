package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FarmerTunnelsDto;
import pl.farmapp.backend.entity.FarmerTunnels;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.FarmerTunnelsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerTunnelsService {

    private final FarmerTunnelsRepository farmerTunnelsRepository;
    private final FarmerRepository farmerRepository;

    public FarmerTunnelsService(FarmerTunnelsRepository farmerTunnelsRepository,
                                FarmerRepository farmerRepository) {
        this.farmerTunnelsRepository = farmerTunnelsRepository;
        this.farmerRepository = farmerRepository;
    }

    public List<FarmerTunnels> getAll() {
        return farmerTunnelsRepository.findAll();
    }

    public Optional<FarmerTunnels> getById(Integer id) {
        return farmerTunnelsRepository.findById(id);
    }

    public Optional<FarmerTunnels> create(FarmerTunnels farmerTunnels) {
        // Sprawdź czy farmer istnieje
        if (farmerTunnels.getFarmer() == null
                || farmerTunnels.getFarmer().getId() == null
                || !farmerRepository.existsById(farmerTunnels.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(farmerTunnelsRepository.save(farmerTunnels));
    }

    public Optional<FarmerTunnels> update(Integer id, FarmerTunnels updated) {
        return farmerTunnelsRepository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            existing.setYear(updated.getYear());
            existing.setTunnelsCount(updated.getTunnelsCount());
            return Optional.of(farmerTunnelsRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        farmerTunnelsRepository.deleteById(id);
    }

    public List<FarmerTunnelsDto> getTunnelsByFarmer(Integer farmerId) {
        return farmerTunnelsRepository.findByFarmerId(farmerId)
                .stream()
                .map(ft -> new FarmerTunnelsDto(
                        ft.getYear(),
                        ft.getTunnelsCount()
                ))
                .collect(Collectors.toList());
    }
}
