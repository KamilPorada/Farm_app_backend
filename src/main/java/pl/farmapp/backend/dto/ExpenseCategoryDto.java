package pl.farmapp.backend.dto;

public class ExpenseCategoryDto {

    private Integer id;
    private String name;
    private String icon;
    private Boolean productionCost;
    private Integer seasonYear;

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

    public Boolean getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(Boolean productionCost) {
        this.productionCost = productionCost;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }
}