package pl.farmapp.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "app_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmer_id", nullable = false, unique = true)
    private Long farmerId;

    // ===== PODSTAWOWE =====
    @Column(nullable = false)
    private String language; // pl | en

    @Column(name = "weight_unit", nullable = false)
    private String weightUnit; // kg | t

    @Column(nullable = false)
    private String currency; // PLN | EUR

    @Column(name = "date_format", nullable = false)
    private String dateFormat; // DD-MM-YYYY

    // ===== FORMATOWANIE LICZB =====
    @Column(name = "use_thousands_separator", nullable = false)
    private Boolean useThousandsSeparator;

    // ===== DODATKOWE =====
    @Column(name = "box_weight_kg", precision = 6, scale = 2)
    private BigDecimal boxWeightKg;

    // ===== POWIADOMIENIA =====
    @Column(name = "notifications_enabled", nullable = false)
    private Boolean notificationsEnabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
