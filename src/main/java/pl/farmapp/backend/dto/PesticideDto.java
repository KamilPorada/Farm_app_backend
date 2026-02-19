package pl.farmapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesticideDto {

    private Integer id;
    private Integer pesticideTypeId;
    private String name;
    private Boolean isLiquid;
    private String targetPest;
    private Integer carenceDays;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPesticideTypeId() {
        return pesticideTypeId;
    }

    public void setPesticideTypeId(Integer pesticideTypeId) {
        this.pesticideTypeId = pesticideTypeId;
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

    public void setIsLiquid(Boolean liquid) {
        isLiquid = liquid;
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
