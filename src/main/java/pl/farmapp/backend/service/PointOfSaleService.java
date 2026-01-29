package pl.farmapp.backend.service;

import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.PointOfSaleDto;
import pl.farmapp.backend.entity.Farmer;
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

    public List<PointOfSale> getByFarmer(Integer farmerId) {
        return pointOfSaleRepository.findByFarmer_Id(farmerId);
    }

    public Optional<PointOfSale> getById(Integer id) {
        return pointOfSaleRepository.findById(id);
    }

    public Optional<PointOfSale> create(PointOfSaleDto dto) {

        Farmer farmer = farmerRepository.findById(dto.getFarmerId())
                .orElse(null);

        if (farmer == null) {
            return Optional.empty();
        }

        PointOfSale pos = new PointOfSale();
        pos.setFarmer(farmer);
        pos.setName(dto.getName());
        pos.setAddress(dto.getAddress());
        pos.setType(dto.getType());
        pos.setEmail(dto.getEmail());
        pos.setPhone(dto.getPhone());
        pos.setLatitude(dto.getLatitude());
        pos.setLongitude(dto.getLongitude());

        return Optional.of(pointOfSaleRepository.save(pos));
    }


    public Optional<PointOfSale> update(Integer id, PointOfSale updated) {
        return pointOfSaleRepository.findById(id).map(existing -> {

            existing.setName(updated.getName());
            existing.setAddress(updated.getAddress());
            existing.setType(updated.getType());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setLatitude(updated.getLatitude());
            existing.setLongitude(updated.getLongitude());

            return pointOfSaleRepository.save(existing);
        });
    }

    public void delete(Integer id) {
        pointOfSaleRepository.deleteById(id);
    }
}
