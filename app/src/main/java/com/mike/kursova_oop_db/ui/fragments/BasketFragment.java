package com.mike.kursova_oop_db.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.ui.adapters.BasketAdapter;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

public class BasketFragment extends Fragment {

    private ShopViewModel viewModel;

    private RecyclerView adapterView;
    private TextView basketAllSum;
    private Button basketSend;
    private BasketAdapter adapterBasket;

    int sum = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_basket, container, false);

        viewModel.getBasket().observe(getViewLifecycleOwner(), basket -> showBasket());

        initView(root);
        initAction();
        showBasket();

        //initViewModel();
        //showItem(viewModel.mGoodItem);

        return root;
    }

    private void initView(View v ){
        adapterView = v.findViewById(R.id.adapter);
        basketAllSum = v.findViewById(R.id.basketAllSum);
        basketSend = v.findViewById(R.id.basketSend);
    }

    private void initAction(){
        basketSend.setOnClickListener(v -> {
            viewModel.createOrder(getContext());
            //TODO: создать заказ, очистить корзину
            //basketSend.setBackgroundColor(getResources().getColor(R.color.gray));
            //viewModel.addToBasket(getContext());
        });
    }

    private void showBasket(){
        basketAllSum.setText(viewModel.mBasket.getPrise()+" $");

        initRecyclerView();
    }

    private void initRecyclerView() {
        OnAdapterClickListener listener = new OnAdapterClickListener() {
            @Override
            public void onChangeCount(BasketItems item, int count) {
                viewModel.updateBasketItem(getActivity(), item, count);
            }

            @Override
            public void onDelete(BasketItems item) {
                viewModel.deleteBasketItem(getActivity(), item);
                viewModel.initBasket(getContext());
            }
        };

        adapterView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBasket = new BasketAdapter(getContext());
        adapterBasket.setItems(viewModel.mBasket.getGoods());
        adapterView.setAdapter(adapterBasket);
        adapterBasket.setUpdateDataListener(listener);
    }

    public interface OnAdapterClickListener {
        void onChangeCount(BasketItems item, int count);
        void onDelete(BasketItems item);
    }
}