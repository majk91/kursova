package com.mike.kursova_oop_db.data.models;

public class User extends BaseDBModel{

    public enum Status{
        ADMIN,
        GUEST,
        USER,
        MODERATOR,
        DELIVER
    }

    public static String TABLE_NAME = "user";
    public static String NAME = "name";
    public static String EMAIL = "email";
    public static String PASS = "pass";
    public static String PHOTO = "photo";
    public static String ROLE = "role";

    private String name = "";
    private String email = "";
    private String pass = "";
    private String photo = "";
    private Status role = Status.GUEST;

    @Override
    public String toString() {
        return "User{id=" + id +", name=" + name + ", email=" + email + "}";
    }

    public User() {}

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public User(String name, String email, String pass, String photo, Status role) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.photo = photo;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Status getRole() {
        return role;
    }

    public void setRole(Status role) {
        this.role = role;
    }
}
