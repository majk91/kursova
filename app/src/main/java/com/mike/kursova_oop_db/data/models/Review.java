package com.mike.kursova_oop_db.data.models;

public class Review extends BaseDBModel  {
    public static String TABLE_NAME = "review";
    public static String USER_ID = "u_id";
    public static String GOOD_ID = "g_id";
    public static String RATING = "rating";
    public static String MESSAGE = "msg";
    public static String TIME = "time";


    private long userId = -1;
    private int rating = 0;
    private String msg = "";
    private long goodId = -1;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodsId) {
        this.goodId = goodsId;
    }
}
