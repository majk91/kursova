package com.mike.kursova_oop_db.data.models;

public abstract class BaseDBModel {

    public static String _ID = "_id";

    protected long id=-1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseModel{" + "id=" + id +"}";
    }
}
