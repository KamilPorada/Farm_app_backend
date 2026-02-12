package pl.farmapp.backend.dto;

import java.time.LocalDate;

public class CultivationCalendarDto {

    private Integer id;
    private Integer farmerId;
    private Integer seasonYear;

    private LocalDate prickingStartDate;
    private LocalDate prickingEndDate;

    private LocalDate plantingStartDate;
    private LocalDate plantingEndDate;

    private LocalDate harvestStartDate;
    private LocalDate harvestEndDate;

    public CultivationCalendarDto() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFarmerId() { return farmerId; }
    public void setFarmerId(Integer farmerId) { this.farmerId = farmerId; }

    public Integer getSeasonYear() { return seasonYear; }
    public void setSeasonYear(Integer seasonYear) { this.seasonYear = seasonYear; }

    public LocalDate getPrickingStartDate() { return prickingStartDate; }
    public void setPrickingStartDate(LocalDate prickingStartDate) { this.prickingStartDate = prickingStartDate; }

    public LocalDate getPrickingEndDate() { return prickingEndDate; }
    public void setPrickingEndDate(LocalDate prickingEndDate) { this.prickingEndDate = prickingEndDate; }

    public LocalDate getPlantingStartDate() { return plantingStartDate; }
    public void setPlantingStartDate(LocalDate plantingStartDate) { this.plantingStartDate = plantingStartDate; }

    public LocalDate getPlantingEndDate() { return plantingEndDate; }
    public void setPlantingEndDate(LocalDate plantingEndDate) { this.plantingEndDate = plantingEndDate; }

    public LocalDate getHarvestStartDate() { return harvestStartDate; }
    public void setHarvestStartDate(LocalDate harvestStartDate) { this.harvestStartDate = harvestStartDate; }

    public LocalDate getHarvestEndDate() { return harvestEndDate; }
    public void setHarvestEndDate(LocalDate harvestEndDate) { this.harvestEndDate = harvestEndDate; }
}
