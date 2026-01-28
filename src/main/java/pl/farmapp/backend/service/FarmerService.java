package pl.farmapp.backend.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

        try {
            return tryCreate(externalId, dto);
        } catch (DataIntegrityViolationException e) {
            // ktoś już stworzył usera równolegle
            return getExisting(externalId);
        }
    }

    @Transactional
    protected Farmer tryCreate(String externalId, FarmerProfileDto dto) {
        Farmer farmer = new Farmer();
        farmer.setExternalId(externalId);
        farmer.setName(dto.name());
        farmer.setSurname(dto.surname());
        farmer.setEmail(dto.email());

        return farmerRepository.saveAndFlush(farmer);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Farmer getExisting(String externalId) {
        return farmerRepository.findByExternalId(externalId)
                .orElseThrow(() ->
                        new IllegalStateException("Farmer exists but cannot be loaded"));
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
