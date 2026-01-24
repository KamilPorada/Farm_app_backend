package pl.farmapp.backend.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.dto.FarmerProfileDto;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.repository.FarmerRepository;

import java.util.Optional;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    /* ===================== */
    /* AUTH / ME FLOW        */
    /* ===================== */

    public Optional<Farmer> getMyProfile(Jwt jwt) {
        String externalId = jwt.getSubject();
        return farmerRepository.findByExternalId(externalId);
    }

    public Farmer createMyProfile(Jwt jwt, FarmerProfileDto dto) {
        String externalId = jwt.getSubject();

        if (farmerRepository.existsByExternalId(externalId)) {
            throw new IllegalStateException("Farmer already exists");
        }

        Farmer farmer = new Farmer();
        farmer.setExternalId(externalId);
        farmer.setName(dto.name());
        farmer.setSurname(dto.surname());
        farmer.setEmail(dto.email());

        return farmerRepository.save(farmer);
    }

    public Farmer updateMyProfile(Jwt jwt, FarmerProfileDto dto) {
        Farmer farmer = farmerRepository.findByExternalId(jwt.getSubject())
                .orElseThrow(() -> new IllegalStateException("Farmer not found"));

        farmer.setName(dto.name());
        farmer.setSurname(dto.surname());
        farmer.setEmail(dto.email());

        return farmerRepository.save(farmer);
    }
}
