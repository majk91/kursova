package com.mike.kursova_oop_db;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.mike.kursova_oop_db.data.Utils;
import com.mike.kursova_oop_db.ui.viewmodel.AuthViewModel;

public class AuthRegisterActivity extends AppCompatActivity {

    public static final int PERMISSION_WRITE = 0;
    private AuthViewModel viewModel;
    private NavController navController;

    private TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
        //hasPermissions(this, android.Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE);

        Utils.getInstance().createAdmin(getApplication());
        Utils.getInstance().createDemoShop(getApplication());

        setContentView(R.layout.activity_auth_register);


        errorView=findViewById(R.id.error);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getNavigation().observe(this, nav_event -> {
            if(nav_event.peekContent() != null)  navController.navigate(nav_event.peekContent().first);
        });
        viewModel.getNavBack().observe(this, nav_event -> {
            navController.popBackStack();
        });
        viewModel.getAuthEvent().observe(this, auth -> {
            if(auth.peekContent()){
                showError("", View.GONE);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            showError(viewModel.lastError, View.VISIBLE);
        });

        initNavigation();
    }

    private void initNavigation() {
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).getNavController();
        navController.setGraph(R.navigation.nav_login);
    }

    private void showError(String msg, int visibility) {
        errorView.setText(msg);
        errorView.setVisibility(visibility);
    }

    /*public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }*/

    public boolean checkPermission() {
        String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, 21 );
        } else {
            return  true;
        }
        return false;
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}