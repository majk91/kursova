package com.mike.kursova_oop_db.ui.viewmodel;

import android.content.Context;
import android.util.Pair;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mike.kursova_oop_db.data.Event;
import com.mike.kursova_oop_db.data.engines.PreferenceEngine;
import com.mike.kursova_oop_db.data.models.User;

import io.reactivex.disposables.Disposable;

public class AuthViewModel extends BaseViewModel {

    protected final MutableLiveData<Event<Boolean>> logIn;

    public Disposable dUser;

    public String lastError="";

    public AuthViewModel() {
        logIn = new MutableLiveData<>();
    }

    public LiveData<Event<Boolean>> getNavBack() {return navigateBack;}

    public LiveData<Event<Boolean>> getAuthEvent() {return logIn;}

    public boolean validEmpty(EditText field){
        if(field.getText().toString().isEmpty())
            return false;
        return true;
    }

    public void navTo(int navFragmentId){
        navigateFragmentIndex.postValue(new Event<>(Pair.create(navFragmentId, null)));
    }

    public void auth(final Context ctx, String email, String pass){
        if(dUser!=null) return;
        dUser = dataShopInteractor.getUserByEmail(ctx, email.toLowerCase()).subscribe(user -> {
            if(user.getId()<=0){
                lastError = "User not found";
                authResult(ctx, null, false);
                return;
            }
            if(!pass.equals(user.getPass())){
                lastError = "Password not correct";
                authResult(ctx, null, false);
                return;
            }
            authResult(ctx, user, true);
        }, error -> {
            lastError = error.getMessage();
            authResult(ctx, null, false);
        });
    }

    public void reg(final Context ctx, String name, String email, String pass){
        if(dUser!=null) return;
        dUser = dataShopInteractor.getUserByEmail(ctx, email.toLowerCase()).subscribe(user -> {
            if(user.getId()>0){
                lastError = "User exist. Please log in";
                authResult(ctx, null, false);
                return;
            }
            User u = new User(name, email, pass);
            Disposable d = dataShopInteractor.storeUser(ctx, u).subscribe(saved->{
                if(saved>0){
                    u.setId(saved);
                    authResult(ctx, u, true);
                }
            });

        }, error -> {
            lastError = error.getMessage();
            authResult(ctx, null, false);
        });
    }

    private void authResult(Context ctx, User user, Boolean isAuth){
        dUser.dispose();
        dUser=null;
        if(user !=null){
            PreferenceEngine.getInstance().storeUserId(ctx, user.getId());
            PreferenceEngine.getInstance().storeUserName(ctx, user.getName());
            PreferenceEngine.getInstance().storeUserEmail(ctx, user.getEmail());
            PreferenceEngine.getInstance().storeUserRole(ctx, user.getRole().name());
        }
        logIn.postValue(new Event<>(isAuth));
    }
}
