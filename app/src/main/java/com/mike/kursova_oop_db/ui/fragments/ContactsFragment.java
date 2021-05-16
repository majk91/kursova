package com.mike.kursova_oop_db.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.engines.PreferenceEngine;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.data.models.User;
import com.mike.kursova_oop_db.data.repositories.DatabaseRepoImpl;
import com.mike.kursova_oop_db.data.repositories.base.DatabaseRepository;
import com.mike.kursova_oop_db.ui.adapters.FormAdapter;
import com.mike.kursova_oop_db.ui.viewmodel.ShopViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactsFragment extends Fragment {

    Button send;
    TextInputLayout nameWrap, emailWrap, phoneWrap, msgWrap;
    EditText name, email, phone, msg;
    TextView info;
    LinearLayout formWrap;

    RecyclerView adapter;
    private FormAdapter adapterForms;
    private ShopViewModel viewModel;


    private DatabaseRepository databaseRepository;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        viewModel.getForms().observe(getViewLifecycleOwner(), this::initRecyclerView);
        databaseRepository =  new DatabaseRepoImpl();

        formWrap = root.findViewById(R.id.formWrap);
        send = root.findViewById(R.id.button);
        nameWrap = root.findViewById(R.id.nameParent);
        emailWrap = root.findViewById(R.id.emailParent);
        phoneWrap = root.findViewById(R.id.phoneParent);
        msgWrap = root.findViewById(R.id.msgParent);
        name = root.findViewById(R.id.nameView);
        email = root.findViewById(R.id.emailView);
        phone = root.findViewById(R.id.phoneView);
        msg = root.findViewById(R.id.msgView);
        info = root.findViewById(R.id.info);
        adapter = root.findViewById(R.id.adapter);

        send.setOnClickListener(v -> {
            clearForm();
            Boolean valid = true;
            if(name.getText().toString().isEmpty()){
                valid = false;
                nameWrap.setError("Empty!");
            }
            if(email.getText().toString().isEmpty()){
                valid = false;
                emailWrap.setError("Empty!");
            }
            if(phone.getText().toString().isEmpty()){
                valid = false;
                phoneWrap.setError("Empty!");
            }

            if(valid){
                saveComment();
            }

        });

        if( User.Status.valueOf(PreferenceEngine.getInstance().getUserRole(getContext())) == User.Status.ADMIN){
            viewModel.initFormsToAdmin(getContext());
            formWrap.setVisibility(View.GONE);
            adapter.setVisibility(View.VISIBLE);
        }else{
            formWrap.setVisibility(View.VISIBLE);
            adapter.setVisibility(View.GONE);
        }

        return root;
    }

    public void saveComment() {
        info.setText("Success send!");
        info.setVisibility(View.VISIBLE);
        String valueName = name.getText().toString();
        String valueEmail = email.getText().toString();
        String valuePhone = phone.getText().toString();
        String valueMsg = msg.getText().toString();

        FormMsg f = new FormMsg(valueName, valuePhone, valueEmail, valueMsg);
        databaseRepository.storeForm(getContext(), f).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe( );

    }

    public void clearForm(){
        nameWrap.setError(null);
        emailWrap.setError(null);
        phoneWrap.setError(null);
        info.setVisibility(View.GONE);
    }

    private void initRecyclerView(List<FormMsg> msgList) {
        adapter.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterForms = new FormAdapter(getContext());
        adapterForms.setItems(msgList);
        adapter.setAdapter(adapterForms);
    }

}