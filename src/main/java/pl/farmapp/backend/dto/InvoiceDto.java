package pl.farmapp.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDto {

    private Integer id;
    private Integer pointOfSaleId;
    private LocalDate invoiceDate;
    private String invoiceNumber;
    private BigDecimal amount;
    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPointOfSaleId() {
        return pointOfSaleId;
    }

    public void setPointOfSaleId(Integer pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
