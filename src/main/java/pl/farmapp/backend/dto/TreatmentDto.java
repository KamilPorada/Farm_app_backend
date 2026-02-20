package pl.farmapp.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentDto {

    private Integer id;
    private Integer farmerId;
    private Integer pesticideId;
    private LocalDate treatmentDate;
    private LocalTime treatmentTime;
    private BigDecimal pesticideDose;
    private BigDecimal liquidVolume;
    private Integer tunnelCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Integer farmerId) {
        this.farmerId = farmerId;
    }

    public Integer getPesticideId() {
        return pesticideId;
    }

    public void setPesticideId(Integer pesticideId) {
        this.pesticideId = pesticideId;
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
