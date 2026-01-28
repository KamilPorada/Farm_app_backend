package pl.farmapp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.farmapp.backend.dto.FarmerTunnelsDto;
import pl.farmapp.backend.dto.FarmerTunnelsSyncRequest;
import pl.farmapp.backend.service.FarmerTunnelsService;

import java.util.List;

@RestController
@RequestMapping("/api/farmer-tunnels")
@RequiredArgsConstructor
public class FarmerTunnelsController {

    private final FarmerTunnelsService service;

    public FarmerTunnelsController(FarmerTunnelsService service) {
        this.service = service;
    }

    /* ===== GET ===== */
    @GetMapping("/{farmerId}")
    public ResponseEntity<List<FarmerTunnelsDto>> getFarmerTunnels(
            @PathVariable Integer farmerId
    ) {
        return ResponseEntity.ok(service.getFarmerTunnels(farmerId));
    }

    /* ===== SYNC (ADD / UPDATE / DELETE) ===== */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncFarmerTunnels(
            @RequestBody FarmerTunnelsSyncRequest request
    ) {
        System.out.println("SYNC HIT: " + request.getFarmerId());
        service.syncFarmerTunnels(request);
        return ResponseEntity.ok().build();
    }
}
