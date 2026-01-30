package pl.farmapp.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradeOfPepperDto {

    public Integer id;
    public Integer farmerId;
    public Integer pointOfSaleId;
    public LocalDate tradeDate;
    public Integer pepperClass;
    public String pepperColor;
    public BigDecimal tradePrice;
    public BigDecimal tradeWeight;
    public Integer vatRate;

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

    public Integer getPointOfSaleId() {
        return pointOfSaleId;
    }

    public void setPointOfSaleId(Integer pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
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
