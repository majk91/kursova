package com.mike.kursova_oop_db;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mike.kursova_oop_db.data.engines.PreferenceEngine;
import com.mike.kursova_oop_db.data.models.Basket;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public TextView userName, userEmail;
    public LinearLayout menuHeader;
    public NavigationView mNavigationView;
    private DrawerLayout mDrawer;

    private ShopViewModel viewModel;
    private NavController navController;

    TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PreferenceEngine.getInstance().getUserId(this)<=0){
            Intent intent = new Intent(this, AuthRegisterActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        initViewModel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+380123456789"));
            startActivity(intent);
        });
        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_shop, R.id.nav_basket, R.id.nav_history, R.id.nav_contacts, R.id.nav_logout)
                .setDrawerLayout(mDrawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setHeaderMenu();

        navigationView.setNavigationItemSelectedListener(item -> {
            mDrawer.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.nav_logout)
                logout();
            else
                navController.navigate(item.getItemId());

            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_settings);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        return true;
    }

    public void shoppingCartClickListener(View v) {
        navController.navigate(R.id.nav_basket);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_settings)
            navController.navigate(R.id.nav_basket);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setHeaderMenu(){
        mNavigationView = findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        userName = header.findViewById(R.id.menuUserName);
        userEmail = header.findViewById(R.id.menuUserEmail);
        menuHeader = header.findViewById(R.id.menuHeader);
        userName.setText(PreferenceEngine.getInstance().getUserName(this));
        userEmail.setText(PreferenceEngine.getInstance().getUserEmail(this));

        menuHeader.setOnClickListener(v -> {
            //TODO: go to profile
        });

    }

    private void logout(){
        PreferenceEngine.getInstance().clearUser(this);
        Intent intent = new Intent(this, AuthRegisterActivity.class);
        startActivity(intent);
        return;
    }

    private void initViewModel(){
        viewModel.getNavigation().observe(this, nav -> {
            if(nav.peekContent() != null)  navController.navigate(nav.peekContent().first, nav.peekContent().second);
        });

        viewModel.getBasket().observe(this, basket -> setupBadge(basket));

        viewModel.initBasket(this);
    }

    public void setupBadge(Basket basket){
        if(basket==null) return;
        int cartItem = basket.getCount();
        if (textCartItemCount != null) {
            if (cartItem == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(cartItem, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}