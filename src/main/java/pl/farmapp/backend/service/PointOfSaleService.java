package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.entity.PointOfSale;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.repository.PointOfSaleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PointOfSaleService {

    private final PointOfSaleRepository pointOfSaleRepository;
    private final FarmerRepository farmerRepository;

    public PointOfSaleService(PointOfSaleRepository pointOfSaleRepository,
                              FarmerRepository farmerRepository) {
        this.pointOfSaleRepository = pointOfSaleRepository;
        this.farmerRepository = farmerRepository;
    }

    public List<PointOfSale> getAll() {
        return pointOfSaleRepository.findAll();
    }

    public Optional<PointOfSale> getById(Integer id) {
        return pointOfSaleRepository.findById(id);
    }

    public Optional<PointOfSale> create(PointOfSale pos) {
        if (pos.getFarmer() == null ||
                pos.getFarmer().getId() == null ||
                !farmerRepository.existsById(pos.getFarmer().getId())) {
            return Optional.empty();
        }
        return Optional.of(pointOfSaleRepository.save(pos));
    }

    public Optional<PointOfSale> update(Integer id, PointOfSale updated) {
        return pointOfSaleRepository.findById(id).flatMap(existing -> {

            if (updated.getFarmer() != null && updated.getFarmer().getId() != null) {
                if (!farmerRepository.existsById(updated.getFarmer().getId())) {
                    return Optional.empty();
                }
                existing.setFarmer(updated.getFarmer());
            }

            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setType(updated.getType());
            existing.setLatitude(updated.getLatitude());
            existing.setLongitude(updated.getLongitude());

            return Optional.of(pointOfSaleRepository.save(existing));
        });
    }

    public void delete(Integer id) {
        pointOfSaleRepository.deleteById(id);
    }

    public List<PointOfSale> getByFarmer(Integer farmerId) {
        return pointOfSaleRepository.findByFarmerId(farmerId);
    }
}
