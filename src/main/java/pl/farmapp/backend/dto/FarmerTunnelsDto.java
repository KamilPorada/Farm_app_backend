package pl.farmapp.backend.dto;

import java.math.BigDecimal;

public class FarmerTunnelsDto {

    private Integer year;
    private BigDecimal count;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}
