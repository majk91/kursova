package com.mike.kursova_oop_db.ui.viewmodel;

import android.os.Bundle;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mike.kursova_oop_db.data.Event;
import com.mike.kursova_oop_db.data.repositories.ShopInteractor;

public abstract class BaseViewModel extends ViewModel {

    protected ShopInteractor dataShopInteractor;
    protected final MutableLiveData<Event<Boolean>> navigateBack;
    protected final MutableLiveData<Event<Pair<Integer, Bundle>>> navigateFragmentIndex;

    public BaseViewModel() {
        dataShopInteractor = new ShopInteractor();
        navigateBack = new MutableLiveData<>();
        navigateFragmentIndex = new MutableLiveData<>();
    }

    public void fallback(){
        navigateBack.postValue(new Event<>(true));
    }

    public LiveData<Event<Pair<Integer, Bundle>>> getNavigation() {return navigateFragmentIndex;}

}
