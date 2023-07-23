package com.brutus.abio.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Video extends AbstractEntity {

    private String title_en;
    private String title_ru;
    private String title_am;
    private String description_en;
    private String description_ru;
    private String description_am;
    private String url;

    public String getTitle_en() {
        return title_en;
    }
    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }
    public String getTitle_ru() {
        return title_ru;
    }
    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }
    public String getTitle_am() {
        return title_am;
    }
    public void setTitle_am(String title_am) {
        this.title_am = title_am;
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
