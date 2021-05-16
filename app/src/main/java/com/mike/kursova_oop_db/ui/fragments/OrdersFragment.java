package com.mike.kursova_oop_db.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.ui.adapters.OrdersAdapter;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

import java.util.List;

public class OrdersFragment extends Fragment {

    private ShopViewModel viewModel;
    private RecyclerView adapterView;
    private OrdersAdapter adapterOrder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        View root = inflater.inflate(R.layout.fragment_adapter, container, false);
        initView(root);

        initOrders();

        viewModel.getOrders().observe(getViewLifecycleOwner(), this::initRecyclerView);
        return root;
    }

    private void initView(View v ){
        adapterView = v.findViewById(R.id.adapter);
    }

    private void initOrders(){
        viewModel.initOrders(getContext());
    }

    private void initRecyclerView(List<Order> list) {
        OrdersAdapter.OnSetStatusListener listener = (item, status) -> {
            viewModel.updateOrderStatus(getContext(), item, status);
        };

        adapterView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterOrder = new OrdersAdapter(getContext());
        adapterOrder.setItems(list);
        adapterView.setAdapter(adapterOrder);
        adapterOrder.setUpdateStatusListener(listener);
    }
}