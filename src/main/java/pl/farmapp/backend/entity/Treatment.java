package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "treatment")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesticide_id", nullable = false)
    private Pesticide pesticide;

    private LocalDate treatmentDate;

    private LocalTime treatmentTime;

    private BigDecimal pesticideDose;

    private BigDecimal liquidVolume;

    private Integer tunnelCount;

    public Treatment() {
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

    public Pesticide getPesticide() {
        return pesticide;
    }

    public void setPesticide(Pesticide pesticide) {
        this.pesticide = pesticide;
    }

    public LocalDate getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDate treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public LocalTime getTreatmentTime() {
        return treatmentTime;
    }

    public void setTreatmentTime(LocalTime treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    public BigDecimal getPesticideDose() {
        return pesticideDose;
    }

    public void setPesticideDose(BigDecimal pesticideDose) {
        this.pesticideDose = pesticideDose;
    }

    public BigDecimal getLiquidVolume() {
        return liquidVolume;
    }

    public void setLiquidVolume(BigDecimal liquidVolume) {
        this.liquidVolume = liquidVolume;
    }

    public Integer getTunnelCount() {
        return tunnelCount;
    }

    public void setTunnelCount(Integer tunnelCount) {
        this.tunnelCount = tunnelCount;
    }
}
