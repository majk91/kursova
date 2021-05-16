package com.mike.kursova_oop_db.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.ui.adapters.CategoryAdapter;
import com.mike.kursova_oop_db.ui.adapters.GoodsAdapter;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

import java.util.List;

public class ShopFragmentMain extends Fragment {

    private ShopViewModel viewModel;

    private RecyclerView adapterCategoryView, adapterGoodsView;
    private ImageButton filterBtn;
    private LinearLayout filter;
    private Button sortPriceTop, sortPriceBottom, sortNameTop, sortNameBottom;

    private GoodsAdapter adapterGoods;
    private CategoryAdapter adapterCategory;

    private Long CategoryChousenId=-1L;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shop_main, container, false);

        initView(root);
        initAction();
        initRecyclerView();
        initViewModel();

        viewModel.updateGoods(getActivity(), CategoryChousenId);
        viewModel.updateCategory(getActivity());

        return root;
    }

    public void initViewModel(){
        viewModel.getCategory().observe(getViewLifecycleOwner(), list -> showCategory(list));
        viewModel.getGoods().observe(getViewLifecycleOwner(), list -> showGoods(list));
    }

    public void showCategory(List<Category> list){
        adapterCategory.setItems(list);
    }

    public void showGoods(List<Good> list){
        adapterGoods.setItems(list);
    }

    private void initView(View v ){
        //viewModel.observeBasket();
        adapterCategoryView=v.findViewById(R.id.adapterCategory);
        adapterGoodsView=v.findViewById(R.id.adapter);
        filterBtn=v.findViewById(R.id.imageButton);
        filter=v.findViewById(R.id.filter);

        sortPriceTop=v.findViewById(R.id.sortPriceTop);
        sortPriceBottom=v.findViewById(R.id.sortPriceBottom);
        sortNameTop=v.findViewById(R.id.sortNameTop);
        sortNameBottom=v.findViewById(R.id.sortNameBottom);

    }

    private void initAction(){
        filterBtn.setOnClickListener(v -> {
            if(filter.getVisibility() == View.GONE)
                filter.setVisibility(View.VISIBLE);
            else
                filter.setVisibility(View.GONE);
        });

        sortPriceTop.setOnClickListener(v -> {
            filter.setVisibility(View.GONE);
            viewModel.updateGoodsWhithSort(getActivity(), CategoryChousenId, Good.PRICE,  "DESC");
        });

        sortPriceBottom.setOnClickListener(v -> {
            filter.setVisibility(View.GONE);
            viewModel.updateGoodsWhithSort(getActivity(), CategoryChousenId, Good.PRICE,  "ASC");
        });

        sortNameTop.setOnClickListener(v -> {
            filter.setVisibility(View.GONE);
            viewModel.updateGoodsWhithSort(getActivity(), CategoryChousenId, Good.NAME,  "DESC");
        });

        sortNameBottom.setOnClickListener(v -> {
            filter.setVisibility(View.GONE);
            viewModel.updateGoodsWhithSort(getActivity(), CategoryChousenId, Good.NAME,  "ASC");
        });
    }

    private void initRecyclerView() {
        OnAdapterClickListener listener = new OnAdapterClickListener() {
            @Override
            public void onClickCategory(Category item) {
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                CategoryChousenId = item.getId();
                viewModel.updateGoods(getActivity(), CategoryChousenId);
            }

            @Override
            public void onClickGood(Good item) {
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                viewModel.navToGood(item);
            }
        };
        adapterCategoryView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterCategory = new CategoryAdapter(getContext());
        adapterCategoryView.setAdapter(adapterCategory);
        adapterGoodsView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterGoods = new GoodsAdapter(getContext());
        adapterGoodsView.setAdapter(adapterGoods);
        adapterCategory.setAboutDataListener(listener);
        adapterGoods.setAboutDataListener(listener);
    }

    public interface OnAdapterClickListener {
        void onClickCategory(Category item);
        void onClickGood(Good item);
    }

}