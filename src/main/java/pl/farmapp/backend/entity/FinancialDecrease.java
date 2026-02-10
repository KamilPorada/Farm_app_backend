package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import pl.farmapp.backend.entity.FinancialDecreaseType;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_decrease")
public class FinancialDecrease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "farmer_id", nullable = false)
    private Integer farmerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "financial_decrease_type_id",
            nullable = false
    )
    private FinancialDecreaseType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal amount;

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

    public FinancialDecreaseType getType() {
        return type;
    }

    public void setType(FinancialDecreaseType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
