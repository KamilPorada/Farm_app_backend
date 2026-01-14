package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Treatment;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PesticideRepository;
import pl.farmapp.backend.repository.TreatmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

    private final TreatmentRepository repository;
    private final FarmerRepository farmerRepository;
    private final PesticideRepository pesticideRepository;

    public TreatmentService(TreatmentRepository repository,
                            FarmerRepository farmerRepository,
                            PesticideRepository pesticideRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.pesticideRepository = pesticideRepository;
    }

    public List<Treatment> getAll() {
        return repository.findAll();
    }

    public Optional<Treatment> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Treatment> create(Treatment treatment) {
        if (treatment.getFarmer() == null || treatment.getFarmer().getId() == null
                || !farmerRepository.existsById(treatment.getFarmer().getId())) {
            return Optional.empty();
        }
        if (treatment.getPesticide() == null || treatment.getPesticide().getId() == null
                || !pesticideRepository.existsById(treatment.getPesticide().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(treatment));
    }

    public Optional<Treatment> update(Integer id, Treatment updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            if (updated.getPesticide() != null && updated.getPesticide().getId() != null) {
                if (!pesticideRepository.existsById(updated.getPesticide().getId())) {
                    return Optional.empty();
                }
                existing.setPesticide(updated.getPesticide());
            }
            existing.setTreatmentDate(updated.getTreatmentDate());
            existing.setTreatmentTime(updated.getTreatmentTime());
            existing.setPesticideDose(updated.getPesticideDose());
            existing.setLiquidVolume(updated.getLiquidVolume());
            existing.setTunnelCount(updated.getTunnelCount());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Treatment> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<Treatment> getByPesticide(Integer pesticideId) {
        return repository.findByPesticideId(pesticideId);
    }
}
