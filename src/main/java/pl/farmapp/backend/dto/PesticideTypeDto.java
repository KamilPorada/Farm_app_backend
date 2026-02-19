package pl.farmapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesticideTypeDto {

    private Integer id;
    private String name;

    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
