package com.brutus.abio.data.entity;

import jakarta.persistence.Entity;

@Entity
public class DeliveryRegion extends AbstractEntity {

    private String name_en;
    private String name_ru;
    private String name_am;
    private Integer price;
    private String currencyType;
    private Integer bulky;

    public String getName_en() {
        return name_en;
    }
    public void setName_en(String name_en) {
        this.name_en = name_en;
    }
    public String getName_ru() {
        return name_ru;
    }
    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }
    public String getName_am() {
        return name_am;
    }
    public void setName_am(String name_am) {
        this.name_am = name_am;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getCurrencyType() {
        return currencyType;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public Integer getBulky() {
        return bulky;
    }
    public void setBulky(Integer bulky) {
        this.bulky = bulky;
    }

}
