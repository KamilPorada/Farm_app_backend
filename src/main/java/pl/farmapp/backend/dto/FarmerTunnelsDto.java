package pl.farmapp.backend.dto;

import java.math.BigDecimal;

public class FarmerTunnelsDto {

    private Integer year;
    private BigDecimal tunnelsCount;

    public FarmerTunnelsDto(Integer year, BigDecimal tunnelsCount) {
        this.year = year;
        this.tunnelsCount = tunnelsCount;
    }

    public Integer getYear() {
        return year;
    }

    public BigDecimal getTunnelsCount() {
        return tunnelsCount;
    }
}
