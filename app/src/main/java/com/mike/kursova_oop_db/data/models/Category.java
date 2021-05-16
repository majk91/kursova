package com.mike.kursova_oop_db.data.models;

import org.jetbrains.annotations.NotNull;

public class Category extends BaseDBModel {
    public static String TABLE_NAME = "category";
    public static String NAME = "name";
    public static String PHOTO = "photo";
    public static String ACTIVE = "active";

    private String name="";
    private String photoURI="";//URL в реальной базе
    private Boolean active = false;
    private Boolean activeInUi = false;

    public Category() {
    }

    public Category(String name, String photoURI, Boolean active) {
        this.name = name;
        this.photoURI = photoURI;
        this.active = active;
    }

    @NotNull
    public String toString() {
        return String.format("name:%s [id:%s]", this.name, this.id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActiveInUi() {
        return activeInUi;
    }

    public void setActiveInUi(Boolean activeInUi) {
        this.activeInUi = activeInUi;
    }
}
