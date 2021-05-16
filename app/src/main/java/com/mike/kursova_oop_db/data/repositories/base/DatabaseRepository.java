package com.mike.kursova_oop_db.data.repositories.base;

import android.content.Context;

import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.OrderItem;
import com.mike.kursova_oop_db.data.models.Review;
import com.mike.kursova_oop_db.data.models.User;

import java.util.List;

import io.reactivex.Single;

public interface DatabaseRepository {
    //USER
    Single<Long> storeUser(final Context context, final User user);
    Single<Boolean> deleteUser(final Context context, final int id);
    Single<User> getUserById(final Context ctx, final long id);
    Single<User> getUserByEmail(final Context ctx, final String email);

    //BASKET
    Single<Long> storeBasketItem(final Context ctx, final BasketItems basket, final long userId);
    Single<Boolean> deleteBasket(final Context context, final long userId);
    Single<Boolean> deleteBasketItem(final Context context, final BasketItems basket);
    Single<Basket> getBasketByUserId(final Context ctx, final long userId);

    //CATEGORY
    Single<Long> storeCategory(final Context ctx, final Category category);
    Single<Boolean> deleteCategory(final Context context, final long id);
    Single<List<Category>> getCategories(final Context ctx);

    //FORM
    Single<Long> storeForm(final Context ctx, final FormMsg form);
    Single<Boolean> deleteForm(final Context context, final int id);
    Single<List<FormMsg>> getForms(final Context ctx);

    //GOODS
    Single<Long> storeGood(final Context ctx, final Good good);
    Single<Boolean> deleteGood(final Context context, final long id);
    Single<Good> getGoodById(final Context ctx, final long id);
    Single<List<Good>> getGoods(final Context ctx);
    Single<List<Good>> getGoodsByCategory(final Context ctx, final long categoryId);
    Single<List<Good>> getGoodsByCategoryIdSort(final Context ctx, final long categoryId, final String sortBy, final String order);
    Single<List<Good>> getGoodsBySort(final Context ctx, final String sortBy, final String order);

    //ORDER ITEM
    Single<Long> storeOrderItem(final Context ctx, final OrderItem form);
    //Single<Boolean> deleteOrderItem(final Context context, final int id);
    //Single<List<OrderItem>> getOrderItemsByParentOrderId(final Context ctx);


    //ORDER
    Single<Long> storeOrderAll(final Context ctx, final Order order);
    Single<Long> storeOrder(final Context ctx, final Order order);
    Single<Boolean> deleteOrder(final Context context, final int id);
    Single<Long> updateStatus(final Context ctx, final long orderId, final Order.Status status);
    Single<List<Order>> getOrders(final Context ctx);
    Single<List<Order>> getOrdersByUserId(final Context ctx, final long userId);


    //REVIEW
    Single<Long> storeReview(final Context ctx, final Review review);
    Single<Boolean> deleteReview(final Context context, final int id);
    Single<List<Review>> getReviewsByGoodId(final Context ctx, final long goodId);

}