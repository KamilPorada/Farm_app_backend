package pl.farmapp.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class FarmerDetailsDto {

    public Long farmerId;

    public String voivodeship;
    public String district;
    public String commune;
    public String locality;

    public BigDecimal farmAreaHa;
    public String settlementType;

    public String crops;
}
