package pl.farmapp.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSettingsDto {

    private Long farmerId;

    private String language;
    private String weightUnit;
    private String currency;
    private String dateFormat;

    private Boolean useThousandsSeparator;

    private BigDecimal boxWeightKg;

    private Boolean notificationsEnabled;

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Boolean getUseThousandsSeparator() {
        return useThousandsSeparator;
    }

    public void setUseThousandsSeparator(Boolean useThousandsSeparator) {
        this.useThousandsSeparator = useThousandsSeparator;
    }

    public BigDecimal getBoxWeightKg() {
        return boxWeightKg;
    }

    public void setBoxWeightKg(BigDecimal boxWeightKg) {
        this.boxWeightKg = boxWeightKg;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
}
