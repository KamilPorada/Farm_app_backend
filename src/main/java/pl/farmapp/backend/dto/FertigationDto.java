package pl.farmapp.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FertigationDto {

    private Integer id;
    private Integer fertilizerId;
    private LocalDate fertigationDate;
    private BigDecimal dose;
    private Integer tunnelCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFertilizerId() {
        return fertilizerId;
    }

    public void setFertilizerId(Integer fertilizerId) {
        this.fertilizerId = fertilizerId;
    }

    public LocalDate getFertigationDate() {
        return fertigationDate;
    }

    public void setFertigationDate(LocalDate fertigationDate) {
        this.fertigationDate = fertigationDate;
    }

    public BigDecimal getDose() {
        return dose;
    }

    public void setDose(BigDecimal dose) {
        this.dose = dose;
    }

    public Integer getTunnelCount() {
        return tunnelCount;
    }

    public void setTunnelCount(Integer tunnelCount) {
        this.tunnelCount = tunnelCount;
    }
}