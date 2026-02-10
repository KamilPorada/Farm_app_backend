package pl.farmapp.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "financial_increase_type",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"farmer_id", "name", "season_year"}
        )
)
public class FinancialIncreaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "farmer_id", nullable = false)
    private Integer farmerId;

    @Column(nullable = false)
    private String name;

    @Column(name = "season_year", nullable = false)
    private Integer seasonYear;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }
}
