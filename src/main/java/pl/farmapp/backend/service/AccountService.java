package pl.farmapp.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.farmapp.backend.repository.FarmerRepository;
import pl.farmapp.backend.service.kinde.KindeUserService;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final FarmerRepository farmerRepository;
    private final KindeUserService kindeUserService;

    public AccountService(FarmerRepository farmerRepository, KindeUserService kindeUserService) {
        this.farmerRepository = farmerRepository;
        this.kindeUserService = kindeUserService;
    }

    @Transactional
    public void deleteAccount(String externalUserId) {

        // 1️⃣ znajdź farmera po externalId
        var farmer = farmerRepository.findByExternalId(externalUserId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        // 2️⃣ usuń usera w Kinde
        kindeUserService.deleteUser(externalUserId);

        // 3️⃣ usuń farmera (CASCADE zrobi resztę)
        farmerRepository.deleteById(farmer.getId());
    }
}
