package com.mike.kursova_oop_db.data.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order extends BaseDBModel {

    public enum Status{
        NEW,
        CANCELED,
        IN_PROGRESS,
        IN_DELIVERY,
        PAID,
        END
    }

    public static String TABLE_NAME = "OrderList";
    public static String GOODS_COUNT = "g_count";
    public static String TIME = "time";
    public static String USER_ID = "u_id";
    public static String STATUS = "status";
    public static String TOTAL_PRISE = "price";

    public static String USER_NAME = "u_name";
    public static String USER_EMAIL = "u_email";

    private int count=1;
    private long userId = -1;
    private double prise = 0;
    private Status status= Status.NEW;
    private String userName = "";
    private String userEmail = "";
    private long time = 0;

    private List<OrderItem> items = new ArrayList<>();

    @NotNull
    public String toString() {
        return String.format("%s: id:%s, count:%s, prise:%s", TABLE_NAME, id, count, prise);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getPrise() {
        return prise;
    }

    public void setPrise(double prise) {
        this.prise = prise;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static Order parse(Basket b){
        Order o = new Order();
        o.setCount(b.getCount());
        o.setUserId(b.getUserId());
        o.setPrise(b.getPrise());

        HashMap<Long, BasketItems> basketItems = b.getGoods();
        basketItems.forEach((k,v)->{
            OrderItem item =  new OrderItem(v);
            o.addItem(item);
        });

        return o;
    }
}
