package pl.farmapp.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HarvestDto {

    private Integer id;
    private Integer varietySeasonId;
    private LocalDate harvestDate;
    private BigDecimal boxCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVarietySeasonId() {
        return varietySeasonId;
    }

    public void setVarietySeasonId(Integer varietySeasonId) {
        this.varietySeasonId = varietySeasonId;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public BigDecimal getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(BigDecimal boxCount) {
        this.boxCount = boxCount;
    }
}
