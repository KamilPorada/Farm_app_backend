package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.repository.FarmerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Optional<Farmer> getFarmerById(Integer id) {
        return farmerRepository.findById(id);
    }

    public Farmer createFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public Optional<Farmer> updateFarmer(Integer id, Farmer updatedFarmer) {
        return farmerRepository.findById(id).map(farmer -> {
            farmer.setName(updatedFarmer.getName());
            farmer.setSurname(updatedFarmer.getSurname());
            farmer.setEmail(updatedFarmer.getEmail());
            return farmerRepository.save(farmer);
        });
    }

    public void deleteFarmer(Integer id) {
        farmerRepository.deleteById(id);
    }
}
