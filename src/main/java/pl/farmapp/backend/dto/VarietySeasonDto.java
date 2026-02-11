package pl.farmapp.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VarietySeasonDto {

    private Integer id;
    private Integer seasonYear;
    private String name;
    private BigDecimal tunnelCount;

    private String color;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTunnelCount() {
        return tunnelCount;
    }

    public void setTunnelCount(BigDecimal tunnelCount) {
        this.tunnelCount = tunnelCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
