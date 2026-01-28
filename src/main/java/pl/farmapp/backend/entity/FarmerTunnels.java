package pl.farmapp.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "farmer_tunnels",
        uniqueConstraints = @UniqueConstraint(columnNames = {"farmer_id", "year"})
)
public class FarmerTunnels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "farmer_id", nullable = false)
    private Integer farmerId;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "tunnels_count", nullable = false, precision = 5, scale = 2)
    private BigDecimal tunnelsCount;

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
