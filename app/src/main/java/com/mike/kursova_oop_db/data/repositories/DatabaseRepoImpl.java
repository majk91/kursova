package com.mike.kursova_oop_db.data.repositories;

import android.content.Context;

import com.mike.kursova_oop_db.data.engines.DatabaseEngine;
import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.OrderItem;
import com.mike.kursova_oop_db.data.models.Review;
import com.mike.kursova_oop_db.data.models.User;
import com.mike.kursova_oop_db.data.repositories.base.DatabaseRepository;

import java.util.List;

import io.reactivex.Single;

public class DatabaseRepoImpl implements DatabaseRepository {

    private final DatabaseEngine databaseEngine;

    public DatabaseRepoImpl() {
        this.databaseEngine = DatabaseEngine.getInstance();
    }

    @Override
    public Single<Long> storeUser(Context ctx, User user) {
        return Single.fromCallable(() -> databaseEngine.storeUser(ctx, user));
    }

    @Override
    public Single<Boolean> deleteUser(Context ctx, int id) {
        return Single.fromCallable(() -> databaseEngine.deleteUser(ctx, id));
    }

    @Override
    public Single<User> getUserById(Context ctx, long id) {
        return Single.fromCallable(() -> databaseEngine.getUserById(ctx, id));
    }

    @Override
    public Single<User> getUserByEmail(Context ctx, String email) {
        return Single.fromCallable(() -> databaseEngine.getUserByEmail(ctx, email));
    }

    @Override
    public Single<Long> storeBasketItem(Context ctx, BasketItems basket, long userId) {
        return Single.fromCallable(() -> databaseEngine.storeBasketItem(ctx, basket, userId));
    }

    @Override
    public Single<Boolean> deleteBasket(Context context, long userId) {
        return Single.fromCallable(() -> databaseEngine.deleteBasket(context, userId));
    }

    @Override
    public Single<Boolean> deleteBasketItem(Context context, BasketItems basket) {
        return Single.fromCallable(() -> databaseEngine.deleteBasketItem(context, basket));
    }

    @Override
    public Single<Basket> getBasketByUserId(Context ctx, long userId) {
        return Single.fromCallable(() -> databaseEngine.getBasketByUserId(ctx, userId));
    }

    @Override
    public Single<Long> storeCategory(Context ctx, Category category) {
        return Single.fromCallable(() -> databaseEngine.storeCategory(ctx, category));
    }

    @Override
    public Single<Boolean> deleteCategory(Context context, long id) {
        return Single.fromCallable(() -> databaseEngine.deleteCategory(context, id));
    }

    @Override
    public Single<List<Category>> getCategories(Context ctx) {
        return Single.fromCallable(() -> databaseEngine.getCategories(ctx));
    }

    @Override
    public Single<Long> storeForm(Context ctx, FormMsg form) {
        return Single.fromCallable(() -> databaseEngine.storeFormMsg(ctx, form));
    }

    @Override
    public Single<Boolean> deleteForm(Context ctx, int id) {
        return Single.fromCallable(() -> databaseEngine.deleteFormMsg(ctx, id));
    }

    @Override
    public Single<List<FormMsg>> getForms(Context ctx) {
        return Single.fromCallable(() -> databaseEngine.getForms(ctx));
    }

    @Override
    public Single<Long> storeGood(Context ctx, Good good) {
        return Single.fromCallable(() -> databaseEngine.storeGood(ctx, good));
    }

    @Override
    public Single<Boolean> deleteGood(Context ctx, long id) {
        return Single.fromCallable(() -> databaseEngine.deleteGood(ctx, id));
    }

    @Override
    public Single<Good> getGoodById(Context ctx, long id) {
        return Single.fromCallable(() -> databaseEngine.getGoodById(ctx, id));
    }

    @Override
    public Single<List<Good>> getGoods(Context ctx) {
        return Single.fromCallable(() -> databaseEngine.getGoods(ctx));
    }

    @Override
    public Single<List<Good>> getGoodsByCategory(Context ctx, long categoryId) {
        return Single.fromCallable(() -> databaseEngine.getGoodsByCategory(ctx, categoryId));
    }

    @Override
    public Single<List<Good>> getGoodsByCategoryIdSort(Context ctx, long categoryId, String sortBy, String order) {
        return Single.fromCallable(() -> databaseEngine.getGoodsByCategoryIdSort(ctx, categoryId, sortBy, order));
    }

    @Override
    public Single<List<Good>> getGoodsBySort(Context ctx, String sortBy, String order) {
        return Single.fromCallable(() -> databaseEngine.getGoodsBySort(ctx, sortBy, order));
    }

    @Override
    public Single<Long> storeOrderItem(Context ctx, OrderItem orderItem) {
        return Single.fromCallable(() -> databaseEngine.storeOrderItem(ctx, orderItem));
    }

    @Override
    public Single<Long> storeOrderAll(Context ctx, Order order) {
        return Single.fromCallable(() -> databaseEngine.storeOrderAll(ctx, order));
    }

    @Override
    public Single<Long> storeOrder(Context ctx, Order order) {
        return Single.fromCallable(() -> databaseEngine.storeOrder(ctx, order));
    }


    @Override
    public Single<Boolean> deleteOrder(Context context, int id) {
        return Single.fromCallable(() -> databaseEngine.deleteOrder(context, id));
    }

    @Override
    public Single<Long> updateStatus(Context ctx, long orderId, Order.Status status) {
        return Single.fromCallable(() -> databaseEngine.updateStatus(ctx, orderId, status));
    }

    @Override
    public Single<List<Order>> getOrders(Context ctx) {
        return Single.fromCallable(() -> databaseEngine.getOrders(ctx));
    }

    @Override
    public Single<List<Order>> getOrdersByUserId(Context ctx, long uId) {
        return Single.fromCallable(() -> databaseEngine.getOrdersByUserId(ctx, uId));
    }

    @Override
    public Single<Long> storeReview(Context ctx, Review review) {
        return Single.fromCallable(() -> databaseEngine.storeReview(ctx, review));
    }

    @Override
    public Single<Boolean> deleteReview(Context context, int id) {
        return Single.fromCallable(() -> databaseEngine.deleteReview(context, id));
    }

    @Override
    public Single<List<Review>> getReviewsByGoodId(Context ctx, long goodId) {
        return Single.fromCallable(() -> databaseEngine.getReviewsByGoodId(ctx, goodId));
    }
}
