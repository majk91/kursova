package com.mike.kursova_oop_db.ui.viewmodel;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.Event;
import com.mike.kursova_oop_db.data.engines.PreferenceEngine;
import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShopViewModel extends BaseViewModel {

    private MutableLiveData<List<Good>> goods;
    private MutableLiveData<List<Category>> category;
    private MutableLiveData<Basket> basket;
    private MutableLiveData<List<Order>> orders;
    private MutableLiveData<List<FormMsg>> formMsgList;

    List<Disposable> d = new ArrayList<>();

    public Good mGoodItem;
    public Basket mBasket;

    public ShopViewModel() {
        goods = new MutableLiveData<>();
        category = new MutableLiveData<>();
        basket = new MutableLiveData<>();
        orders = new MutableLiveData<>();
        formMsgList = new MutableLiveData<>();
    }

    public LiveData<List<Good>> getGoods() {
        return goods;
    }

    public LiveData<List<Category>> getCategory() {
        return category;
    }

    public LiveData<Basket> getBasket() {
        return basket;
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }

    public LiveData<List<FormMsg>> getForms() {
        return formMsgList;
    }

    public void observeBasket(){
        basket.postValue(mBasket);
    }

    public void initBasket(Context ctx){
        long userId = PreferenceEngine.getInstance().getUserId(ctx);
        d.add(
            dataShopInteractor.getBasket(ctx, userId)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
            .subscribe(b->{
                if(b.getPrise()>0)
                    mBasket = b;
                else
                    mBasket = new Basket(userId);
                basket.postValue(mBasket);
            }, error->{
                mBasket = new Basket(userId);
                basket.postValue(mBasket);
            })
        );
    }

    public void updateGoods(Context ctx, Long catId){
        if(catId>0){
            d.add(
                 dataShopInteractor.getGoodsByCategory(ctx, catId).subscribe(list -> {
                    goods.postValue(list);
                 }, error -> {})
            );
        }else {
            d.add(
                dataShopInteractor.getGoods(ctx).subscribe(list -> {
                    goods.postValue(list);
                }, error -> {})
            );
        }

    }

    public void updateGoodsWhithSort(Context ctx, long categoryId, String field,  String order){
        if(categoryId>0){
            d.add(
                dataShopInteractor.getGoodsByCategoryIdSort(ctx, categoryId, field, order).subscribe(list -> {
                    list = customPriseSort(list, field,  order);
                    goods.postValue(list);
                }, error -> {})
            );
        }else{
            d.add(
                dataShopInteractor.getGoodsBySort(ctx, field, order).subscribe(list -> {
                    list = customPriseSort(list, field,  order);
                    goods.postValue(list);
                }, error -> {})
            );
        }
    }

    public void updateCategory(Context ctx){
        d.add(
            dataShopInteractor.getCategories(ctx).subscribe(list -> {
                category.postValue(list);
            }, error -> {})
        );
    }

    public void clearDisposable(){}

    public List<Good> customPriseSort(List<Good> list, String field,  String order ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(order.equals("ASC")){
                if(field.equals(Good.PRICE))
                    Collections.sort(list);

            }else{
                if(field.equals(Good.PRICE))
                    Collections.reverse(list);
            }
        }
        return list;
    }

    public void navToGood(Good item){
        mGoodItem = item;
        //Bundle b = new Bundle();
        //b.putParcelable("good", item);
        navigateFragmentIndex.postValue(new Event<>(Pair.create(R.id.nav_shop_item, null)));
    }

    public void addToBasket(Context ctx){
        mBasket.addGoodToBasket(mGoodItem);
        d.add(dataShopInteractor.storeBasketItem(
                ctx,
                mBasket.getGoodItem(mGoodItem.getId()),
                PreferenceEngine.getInstance().getUserId(ctx)
            ).subscribe(s->basket.postValue(mBasket))
        );
    }

    public void updateBasketItem(Context ctx, BasketItems item, int count){
        item.setCount(count);
        d.add(
            dataShopInteractor.storeBasketItem(
                ctx,
                item,
                PreferenceEngine.getInstance().getUserId(ctx)
            ).subscribe(id->
                initBasket(ctx)
            )
        );
    }

    public void deleteBasketItem(Context ctx, BasketItems item){
        d.add(
            dataShopInteractor.deleteBasketItem(
                ctx,
                item
            ).subscribe(id->
                initBasket(ctx)
            )
        );
    }

    public void deleteBasketByUserId(Context ctx){
        d.add(
            dataShopInteractor.deleteBasket(ctx, PreferenceEngine.getInstance().getUserId(ctx))
            .subscribe(id->
                initBasket(ctx)
            )
        );
    }

    public void createOrder(Context ctx){
        Order order = Order.parse(mBasket);
        d.add(
                dataShopInteractor.storeOrderAll(
                        ctx,
                        order
                ).subscribe( id->deleteBasketByUserId(ctx) )
        );
    }

    public void updateOrderStatus(Context ctx, Order order, Order.Status status){
        //order.setStatus(status);
        d.add(
            dataShopInteractor.updateOrderStatus(
                ctx,
                order.getId(),
                status
            ).subscribe( id->initOrders(ctx) )
        );
    }

    public void initOrders(Context ctx){
        long userId = PreferenceEngine.getInstance().getUserId(ctx);
        User.Status role =  User.Status.valueOf(PreferenceEngine.getInstance().getUserRole(ctx));
        Disposable request;
        if(role == User.Status.ADMIN)
            request =  dataShopInteractor.getOrders(ctx)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list->{
                        orders.postValue(list);
                    }, error->{});
        else
            request =  dataShopInteractor.getOrdersByUserId(ctx, userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list->{
                        orders.postValue(list);
                    }, error->{});

        d.add(request);
    }

    public void initFormsToAdmin(Context ctx){
        Disposable request = dataShopInteractor.getFormList(ctx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    formMsgList.postValue(list);
                }, error->{});
        d.add(request);
    }

}