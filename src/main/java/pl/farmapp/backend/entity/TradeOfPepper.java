package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "trade_of_pepper")
public class TradeOfPepper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_of_sale_id", nullable = false)
    private PointOfSale pointOfSale;

    @Column(name = "trade_date")
    private LocalDate tradeDate;

    @Column(name = "pepper_class")
    private Integer pepperClass;

    @Column(name = "pepper_color")
    private String pepperColor;

    @Column(name = "trade_price")
    private BigDecimal tradePrice;

    @Column(name = "trade_weight")
    private BigDecimal tradeWeight;

    @Column(name = "vat_rate")
    private Integer vatRate;

    public TradeOfPepper() {
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

    public PointOfSale getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(PointOfSale pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getPepperClass() {
        return pepperClass;
    }

    public void setPepperClass(Integer pepperClass) {
        this.pepperClass = pepperClass;
    }

    public String getPepperColor() {
        return pepperColor;
    }

    public void setPepperColor(String pepperColor) {
        this.pepperColor = pepperColor;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public BigDecimal getTradeWeight() {
        return tradeWeight;
    }

    public void setTradeWeight(BigDecimal tradeWeight) {
        this.tradeWeight = tradeWeight;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }
}
