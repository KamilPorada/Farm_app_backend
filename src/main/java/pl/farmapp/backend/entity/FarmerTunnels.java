package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "farmer_tunnels")
public class FarmerTunnels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Klucz obcy do Farmer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    private Integer year;

    @Column(name = "tunnels_count", precision = 10, scale = 2)
    private BigDecimal tunnelsCount;

    // Konstruktor bezparametrowy
    public FarmerTunnels() {
    }

    public FarmerTunnels(Farmer farmer, Integer year, BigDecimal tunnelsCount) {
        this.farmer = farmer;
        this.year = year;
        this.tunnelsCount = tunnelsCount;
    }

    // Gettery i settery

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTunnelsCount() {
        return tunnelsCount;
    }

    public void setTunnelsCount(BigDecimal tunnelsCount) {
        this.tunnelsCount = tunnelsCount;
    }
}
