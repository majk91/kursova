package com.mike.kursova_oop_db.data.models;

import org.jetbrains.annotations.NotNull;

public class OrderItem extends BasketItems {

    public static String TABLE_NAME = "OrderItem";
    public static String GOOD_ID = "g_id";
    public static String GOODS_COUNT = "g_count";
    public static String ORDER_ID = "order_id";

    private long orderId = -1;

    public OrderItem() {}

    public OrderItem(BasketItems item) {
        this.good = item.getGood();
        this.count = item.getCount();
        this.goodId = item.getGoodId();
    }

    @NotNull
    public String toString() {
        return String.format("%s: id:%s, count:%s, good:%s", TABLE_NAME, id, this.count, good.getPrice());
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }


}
