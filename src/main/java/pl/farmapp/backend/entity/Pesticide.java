package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import pl.farmapp.backend.entity.Farmer;
import pl.farmapp.backend.entity.PesticideType;

@Entity
@Table(name = "pesticide")
public class Pesticide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesticide_type_id", nullable = false)
    private PesticideType pesticideType;

    private String name;

    private Boolean isLiquid;

    private String targetPest;

    private Integer carenceDays;

    public Pesticide() {
    }

    // ===== GETTERY I SETTERY =====

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

    public PesticideType getPesticideType() {
        return pesticideType;
    }

    public void setPesticideType(PesticideType pesticideType) {
        this.pesticideType = pesticideType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsLiquid() {
        return isLiquid;
    }

    public void setIsLiquid(Boolean isLiquid) {
        this.isLiquid = isLiquid;
    }

    public String getTargetPest() {
        return targetPest;
    }

    public void setTargetPest(String targetPest) {
        this.targetPest = targetPest;
    }

    public Integer getCarenceDays() {
        return carenceDays;
    }

    public void setCarenceDays(Integer carenceDays) {
        this.carenceDays = carenceDays;
    }
}
