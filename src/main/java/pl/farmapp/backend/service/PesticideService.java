package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Pesticide;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PesticideRepository;
import pl.farmapp.backend.repository.PesticideTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PesticideService {

    private final PesticideRepository repository;
    private final FarmerRepository farmerRepository;
    private final PesticideTypeRepository pesticideTypeRepository;

    public PesticideService(PesticideRepository repository,
                            FarmerRepository farmerRepository,
                            PesticideTypeRepository pesticideTypeRepository) {
        this.repository = repository;
        this.farmerRepository = farmerRepository;
        this.pesticideTypeRepository = pesticideTypeRepository;
    }

    public List<Pesticide> getAll() {
        return repository.findAll();
    }

    public Optional<Pesticide> getById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Pesticide> create(Pesticide pesticide) {
        if (pesticide.getFarmer() == null || pesticide.getFarmer().getId() == null
                || !farmerRepository.existsById(pesticide.getFarmer().getId())) {
            return Optional.empty();
        }
        if (pesticide.getPesticideType() == null || pesticide.getPesticideType().getId() == null
                || !pesticideTypeRepository.existsById(pesticide.getPesticideType().getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(pesticide));
    }

    public Optional<Pesticide> update(Integer id, Pesticide updated) {
        return repository.findById(id).flatMap(existing -> {
            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }
            if (updated.getPesticideType() != null && updated.getPesticideType().getId() != null) {
                if (!pesticideTypeRepository.existsById(updated.getPesticideType().getId())) {
                    return Optional.empty();
                }
                existing.setPesticideType(updated.getPesticideType());
            }
            existing.setName(updated.getName());
            existing.setIsLiquid(updated.getIsLiquid());
            existing.setTargetPest(updated.getTargetPest());
            existing.setCarenceDays(updated.getCarenceDays());
            return Optional.of(repository.save(existing));
        });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Pesticide> getByFarmer(Integer farmerId) {
        return repository.findByFarmerId(farmerId);
    }

    public List<Pesticide> getByPesticideType(Integer pesticideTypeId) {
        return repository.findByPesticideTypeId(pesticideTypeId);
    }
}
