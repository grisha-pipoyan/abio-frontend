package com.brutus.abio.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Header extends AbstractEntity {

    private String headLine_en;
    private String headLine_ru;
    private String headLine_am;
    private String description_en;
    private String description_ru;
    private String description_am;
    private String url;

    public String getHeadLine_en() {
        return headLine_en;
    }
    public void setHeadLine_en(String headLine_en) {
        this.headLine_en = headLine_en;
    }
    public String getHeadLine_ru() {
        return headLine_ru;
    }
    public void setHeadLine_ru(String headLine_ru) {
        this.headLine_ru = headLine_ru;
    }
    public String getHeadLine_am() {
        return headLine_am;
    }
    public void setHeadLine_am(String headLine_am) {
        this.headLine_am = headLine_am;
    }
    public String getDescription_en() {
        return description_en;
    }
    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }
    public String getDescription_ru() {
        return description_ru;
    }
    public void setDescription_ru(String description_ru) {
        this.description_ru = description_ru;
    }
    public String getDescription_am() {
        return description_am;
    }
    public void setDescription_am(String description_am) {
        this.description_am = description_am;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
