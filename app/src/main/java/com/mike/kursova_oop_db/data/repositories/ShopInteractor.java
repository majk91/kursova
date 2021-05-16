package com.mike.kursova_oop_db.data.repositories;

import android.content.Context;

import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.User;
import com.mike.kursova_oop_db.data.repositories.base.DatabaseRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopInteractor {

    private DatabaseRepository databaseRepository;

    public ShopInteractor() {
        this.databaseRepository = new DatabaseRepoImpl();
    }

    public Single<Long> storeUser(final Context ctx, final User user) {
        return databaseRepository.storeUser(ctx, user);
    }

    public Single<Boolean> deleteUser(final Context ctx, final int id) {
        return databaseRepository.deleteUser(ctx, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> getUserById(final Context ctx, final long id) {
        return databaseRepository.getUserById(ctx, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> getUserByEmail(final Context ctx, final String email) {
        return databaseRepository.getUserByEmail(ctx, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> storeCategory(final Context ctx, final Category category) {
        return databaseRepository.storeCategory(ctx, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Category>> getCategories(final Context ctx) {
        return databaseRepository.getCategories(ctx).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> deleteCategory(final Context ctx, final long id) {
        return databaseRepository.deleteCategory(ctx, id);
    }

    public Single<Long> storeGood(final Context ctx, final Good good) {
        return databaseRepository.storeGood(ctx, good).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> deleteGood(final Context ctx, final long id) {
        return databaseRepository.deleteGood(ctx, id);
    }

    public Single<Good> getGoodById(final Context ctx, final long id) {
        return databaseRepository.getGoodById(ctx, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Good>> getGoods(final Context ctx){
        return databaseRepository.getGoods(ctx).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Good>> getGoodsByCategory(final Context ctx, final long categoryId){
        return databaseRepository.getGoodsByCategory(ctx, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Good>> getGoodsByCategoryIdSort(final Context ctx, final long categoryId, final String sortBy, final String order){
        return databaseRepository.getGoodsByCategoryIdSort(ctx, categoryId, sortBy, order) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Good>> getGoodsBySort(final Context ctx, final String sortBy, final String order){
        return databaseRepository.getGoodsBySort(ctx, sortBy, order) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Long> storeBasketItem(final Context ctx, final BasketItems basket, long userId) {
        return databaseRepository.storeBasketItem(ctx, basket, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> deleteBasketItem(final Context ctx, final BasketItems basket) {
        return databaseRepository.deleteBasketItem(ctx, basket).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> deleteBasket(final Context ctx, long userId) {
        return databaseRepository.deleteBasket(ctx, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Basket> getBasket(final Context ctx, long userId) {
        return databaseRepository.getBasketByUserId(ctx, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> storeOrderAll(final Context ctx, final Order order) {
        return databaseRepository.storeOrderAll(ctx, order).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> storeOrder(final Context ctx, final Order order) {
        return databaseRepository.storeOrder(ctx, order).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> updateOrderStatus(final Context ctx, final long orderId, final Order.Status status) {
        return databaseRepository.updateStatus(ctx, orderId, status).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Order>> getOrders(final Context ctx){
        return databaseRepository.getOrders(ctx) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Order>> getOrdersByUserId(final Context ctx, long userId){
        return databaseRepository.getOrdersByUserId(ctx, userId) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<FormMsg>> getFormList(final Context ctx){
        return databaseRepository.getForms(ctx) .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
