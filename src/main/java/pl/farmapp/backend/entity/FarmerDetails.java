package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "farmer_details")
public class FarmerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmer_id", nullable = false, unique = true)
    private Long farmerId;

    @Column(nullable = false, length = 50)
    private String voivodeship;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String commune;

    @Column(length = 100)
    private String locality;

    @Column(name = "farm_area_ha", precision = 10, scale = 2)
    private BigDecimal farmAreaHa;

    @Column(name = "settlement_type", nullable = false, length = 20)
    private String settlementType;

    @Column(columnDefinition = "TEXT")
    private String crops;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setFarmAreaHa(BigDecimal farmAreaHa) {
        this.farmAreaHa = farmAreaHa;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public void setCrops(String crops) {
        this.crops = crops;
    }

    public Long getId() {
        return id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public String getDistrict() {
        return district;
    }

    public String getCommune() {
        return commune;
    }

    public String getLocality() {
        return locality;
    }

    public BigDecimal getFarmAreaHa() {
        return farmAreaHa;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public String getCrops() {
        return crops;
    }

    // getters & setters
}
