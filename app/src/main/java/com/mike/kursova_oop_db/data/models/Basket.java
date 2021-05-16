package com.mike.kursova_oop_db.data.models;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Basket extends BaseDBModel {
    public static String TABLE_NAME = "Basket";
    public static String GOOD_ID = "g_id";
    public static String GOODS_COUNT = "g_count";
    public static String USER_ID = "u_id";

    private int count=0;
    private long userId = -1;
    private Double prise = 0D;

    private HashMap<Long, BasketItems> items = new HashMap<>();

    public Basket() {}

    public Basket(Long userId) {
        this.userId = userId;
    }

    @NotNull
    public String toString() {
        return String.format("%s item: id:%s, count:%s, user_id%s", TABLE_NAME, id, count, userId);
    }

    public HashMap<Long, BasketItems> getGoods() {
        return items;
    }

    public void setGoods(HashMap<Long, BasketItems> items) {
        this.items = items;
        //count = items.size();
    }

    public void addGoodToBasket(Good g){
        if(isGoodInBasket(g))
            items.get(g.getId()).setCount(items.get(g.getId()).getCount()+1);
        else
            items.put(g.getId(), new BasketItems(g,1, g.getId()));

        count++;
    }

    public BasketItems getGoodItem(Long goodId){
        return items.get(goodId);
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

    public Boolean isGoodInBasket(Good good){
        if(items.get(good.getId()) != null )
            return true;

        return false;
    }

    public Double getPrise(){
        prise = 0D;
        HashMap<Long, BasketItems> basketItems = getGoods();
        basketItems.forEach((k,v)->{
            prise += v.getGood().getPrice() * v.getCount();
        });
        return  prise;
    }

}