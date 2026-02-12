package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "cultivation_calendar",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"farmer_id", "season_year"})
        }
)
public class CultivationCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "farmer_id", nullable = false)
    private Integer farmerId;

    @Column(name = "season_year", nullable = false)
    private Integer seasonYear;

    @Column(name = "pricking_start_date")
    private LocalDate prickingStartDate;

    @Column(name = "pricking_end_date")
    private LocalDate prickingEndDate;

    @Column(name = "planting_start_date")
    private LocalDate plantingStartDate;

    @Column(name = "planting_end_date")
    private LocalDate plantingEndDate;

    @Column(name = "harvest_start_date")
    private LocalDate harvestStartDate;

    @Column(name = "harvest_end_date")
    private LocalDate harvestEndDate;

    public CultivationCalendar() {}

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
