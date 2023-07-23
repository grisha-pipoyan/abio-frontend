package com.brutus.abio.data.entity;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class PromoCode extends AbstractEntity {

    private String code;
    private Integer discount;
    private Integer promoCodeType;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private String productCodes;
    private Integer minimumPurchase;
    private Integer maxApplications;
    private Integer currentApplications;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Integer getDiscount() {
        return discount;
    }
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
    public Integer getPromoCodeType() {
        return promoCodeType;
    }
    public void setPromoCodeType(Integer promoCodeType) {
        this.promoCodeType = promoCodeType;
    }
    public LocalDate getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }
    public LocalDate getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }
    public String getProductCodes() {
        return productCodes;
    }
    public void setProductCodes(String productCodes) {
        this.productCodes = productCodes;
    }
    public Integer getMinimumPurchase() {
        return minimumPurchase;
    }
    public void setMinimumPurchase(Integer minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }
    public Integer getMaxApplications() {
        return maxApplications;
    }
    public void setMaxApplications(Integer maxApplications) {
        this.maxApplications = maxApplications;
    }
    public Integer getCurrentApplications() {
        return currentApplications;
    }
    public void setCurrentApplications(Integer currentApplications) {
        this.currentApplications = currentApplications;
    }

}
