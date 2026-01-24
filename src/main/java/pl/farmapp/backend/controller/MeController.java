package pl.farmapp.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FarmerProfileDto;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.service.FarmerService;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final FarmerService farmerService;

    public MeController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    // 1️⃣ GET – sprawdzam czy farmer istnieje
    @GetMapping
    public ResponseEntity<Farmer> getMe(@AuthenticationPrincipal Jwt jwt) {
        return farmerService.getMyProfile(jwt)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2️⃣ POST – tworzę farmera (jeśli nie istnieje)
    @PostMapping
    public ResponseEntity<Farmer> createMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody FarmerProfileDto dto
    ) {
        Farmer created = farmerService.createMyProfile(jwt, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 3️⃣ PUT – aktualizacja profilu
    @PutMapping
    public ResponseEntity<Farmer> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody FarmerProfileDto dto
    ) {
        Farmer updated = farmerService.updateMyProfile(jwt, dto);
        return ResponseEntity.ok(updated);
    }
}
