package pl.farmapp.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FertilizerWithPriceDto {

    public FertilizerWithPriceDto(Integer id, String name, String form, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.form = form;
        this.price = price;
    }

    private Integer id;
    private String name;
    private String form;
    private BigDecimal price;

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

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}