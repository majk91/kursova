package com.mike.kursova_oop_db.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.OrderItem;
import com.mike.kursova_oop_db.data.models.Review;
import com.mike.kursova_oop_db.data.models.User;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shop.db";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TABLE_GOODS = "CREATE TABLE IF NOT EXISTS " +
            Good.TABLE_NAME + "(" +
            Good._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Good.NAME + " TEXT, " +
            Good.VENDOR_CODE + " TEXT, " +
            Good.PHOTO + " TEXT, " +
            Good.PRICE + " TEXT, " +
            Good.DESCRIPTION + " TEXT, " +
            Good.CATEGORY_ID + " INTEGER, " +
            Good.COUNT + " INTEGER, " +
            Good.ACTIVE + " TEXT);";

    private static final String TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS " +
            Category.TABLE_NAME + "(" +
            Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Category.NAME + " TEXT, " +
            Category.PHOTO + " TEXT, " +
            Category.ACTIVE + " TEXT);";

    private static final String TABLE_BASKET = "CREATE TABLE IF NOT EXISTS " +
            Basket.TABLE_NAME + "(" +
            Basket._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Basket.GOOD_ID + " INTEGER, " +
            Basket.GOODS_COUNT + " INTEGER, " +
            Basket.USER_ID + " INTEGER);";

    private static final String TABLE_ORDERS = "CREATE TABLE IF NOT EXISTS " +
            Order.TABLE_NAME + "(" +
            Order._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Order.TOTAL_PRISE + " TEXT, " +
            Order.GOODS_COUNT + " INTEGER, " +
            Order.USER_ID + " INTEGER, " +
            Order.TIME + " INTEGER, " +
            Order.STATUS + " TEXT);";

    private static final String TABLE_ORDERS_ITEMS = "CREATE TABLE IF NOT EXISTS " +
            OrderItem.TABLE_NAME + "(" +
            OrderItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OrderItem.GOOD_ID + " TEXT, " +
            OrderItem.GOODS_COUNT + " INTEGER, " +
            OrderItem.ORDER_ID + " INTEGER);";

    private static final String TABLE_REVIEWS = "CREATE TABLE IF NOT EXISTS " +
            Review.TABLE_NAME + "(" +
            Review._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Review.USER_ID + " INTEGER, " +
            Review.GOOD_ID + " INTEGER, " +
            Review.MESSAGE + " INTEGER, " +
            Review.TIME + " INTEGER, " +
            Review.RATING + " INTEGER);";

    private static final String TABLE_USERS = "CREATE TABLE IF NOT EXISTS " +
            User.TABLE_NAME + "(" +
            User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            User.NAME + " TEXT, " +
            User.EMAIL + " TEXT, " +
            User.PASS + " TEXT, " +
            User.PHOTO + " TEXT, " +
            User.ROLE + " TEXT);";

    private static final String TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS " +
            FormMsg.TABLE_NAME + "(" +
            FormMsg._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FormMsg.NAME + " TEXT, " +
            FormMsg.PHONE + " TEXT, " +
            FormMsg.EMAIL + " TEXT, " +
            FormMsg.TIME + " INTEGER, " +
            FormMsg.MESSAGE + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_GOODS);
        db.execSQL(TABLE_CATEGORY);
        db.execSQL(TABLE_BASKET);
        db.execSQL(TABLE_ORDERS);
        db.execSQL(TABLE_ORDERS_ITEMS);
        db.execSQL(TABLE_REVIEWS);
        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}

