package pl.farmapp.backend.dto;

import java.util.List;

public class FarmerTunnelsSyncRequest {

    private Integer farmerId;
    private List<FarmerTunnelsDto> tunnels;

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public List<FarmerTunnelsDto> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<FarmerTunnelsDto> tunnels) {
        this.tunnels = tunnels;
    }
}
