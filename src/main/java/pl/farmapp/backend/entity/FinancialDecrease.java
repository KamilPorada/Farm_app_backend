package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "financial_decrease")
public class FinancialDecrease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_decrease_type_id", nullable = false)
    private FinancialDecreaseType financialDecreaseType;

    private String title;

    private BigDecimal amount;

    public FinancialDecrease() {
    }

    // ===== GETTERY I SETTERY =====

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

    public FinancialDecreaseType getFinancialDecreaseType() {
        return financialDecreaseType;
    }

    public void setFinancialDecreaseType(FinancialDecreaseType financialDecreaseType) {
        this.financialDecreaseType = financialDecreaseType;
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
