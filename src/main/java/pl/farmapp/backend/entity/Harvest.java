package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "harvest")
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_season_id", nullable = false)
    private VarietySeason varietySeason;

    private LocalDate harvestDate;

    private BigDecimal boxCount;

    public Harvest() {
    }

    // GETTERY / SETTERY

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public VarietySeason getVarietySeason() {
        return varietySeason;
    }

    public void setVarietySeason(VarietySeason varietySeason) {
        this.varietySeason = varietySeason;
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
