package com.mike.kursova_oop_db.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

public class ShopFragmentItem extends Fragment {

    private ShopViewModel viewModel;

    private ImageView realImage, emptyImage;
    private TextView textTitle, category, description;
    private LinearLayout wrap;
    private Button buyBtn, saled;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_good, container, false);

        initView(root);
        initAction();
        initViewModel();

        showItem(viewModel.mGoodItem);

        return root;
    }

    public void initViewModel(){
        //viewModel.getCategory().observe(getViewLifecycleOwner(), list -> showCategory(list));
        //viewModel.getGoods().observe(getViewLifecycleOwner(), list -> showGoods(list));
    }

    private void initView(View v ){
        realImage = v.findViewById(R.id.realImage);
        emptyImage = v.findViewById(R.id.emptyImage);
        textTitle = v.findViewById(R.id.textTitle);
        category = v.findViewById(R.id.category);
        saled = v.findViewById(R.id.saled);
        wrap = v.findViewById(R.id.wrap);
        buyBtn = v.findViewById(R.id.buy);
        description=v.findViewById(R.id.desc);
    }

    private void initAction(){
        buyBtn.setOnClickListener(v -> {
            buyBtn.setBackgroundColor(getResources().getColor(R.color.gray));
            viewModel.addToBasket(getContext());
        });
    }

    private void showItem(Good item) {
        viewModel.observeBasket();
        if(item.getCount()<=0){
            saled.setVisibility(View.VISIBLE);
            buyBtn.setVisibility(View.GONE);
        }else{
            saled.setVisibility(View.GONE);
            buyBtn.setVisibility(View.VISIBLE);
        }

        textTitle.setText(item.getName()+" | "+item.getPrice()+"$");
        category.setText(item.getCatName());
        description.setText(item.getDescription());
        if (!"".equals(item.getPhotoURI())) {
            Glide.with(getContext()).load(item.getPhotoURI()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(realImage);
            realImage.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.GONE);
        }
    }
}