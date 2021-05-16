package com.mike.kursova_oop_db.data.models;

public class BasketItems extends BaseDBModel{

    protected int count = 0;
    protected long goodId = -1;
    protected Good good = new Good();

    public BasketItems() {}

    public BasketItems(Good good, int count, long goodId) {
        this.good = good;
        this.count = count;
        this.goodId = goodId;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }
}
