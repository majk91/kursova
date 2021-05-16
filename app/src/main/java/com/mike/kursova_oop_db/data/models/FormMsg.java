package com.mike.kursova_oop_db.data.models;

public class FormMsg extends BaseDBModel{
    public static String TABLE_NAME = "form";
    public static String NAME = "name";
    public static String PHONE = "phone";
    public static String EMAIL = "email";
    public static String MESSAGE = "msg";
    public static String TIME = "time";


    private String name = "";
    private String phone = "";
    private String email = "";
    private String msg = "";
    private long time = 0;

    public FormMsg() {
    }

    public FormMsg(String name, String phone, String email, String msg) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
